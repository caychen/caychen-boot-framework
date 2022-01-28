package com.caychen.boot.canal.listener;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.exception.CanalClientException;
import com.caychen.boot.canal.config.CanalProperties;
import com.caychen.boot.canal.handler.CanalDataHandler;
import com.caychen.boot.canal.model.CanalColumn;
import com.caychen.boot.canal.model.CanalModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Caychen
 * @Date: 2022/1/28 10:32
 * @Description:
 */
@Slf4j
@Component
public class CanalListener implements InitializingBean, DisposableBean {

    @Autowired
    private CanalProperties canalProperties;

    @Autowired
    private CanalConnector canalConnector;

    @Autowired(required = false)
    private CanalDataHandler canalDataHandler;

    /**
     * 启动
     *
     * @throws Exception
     */
    public void run() {

        while (true) {
            try {
                // 获取指定数量的数据
                Message message = canalConnector.getWithoutAck(canalProperties.getBatchSize());
                //获取批量ID
                long batchId = message.getId();
                //实际抓取的数量
                int size = message.getEntries().size();
                //如果没有数据
                if (batchId == -1 || size == 0) {
                    try {
                        //休眠
                        Thread.sleep(canalProperties.getInterval());
                    } catch (InterruptedException e) {
                        log.error("捕获异常: ", e);
                    }
                } else {
                    //处理数据
                    dealEntry(message.getEntries());
                }

                // 进行 batch id 的确认。确认之后，小于等于此 batchId 的 Message 都会被确认。
                canalConnector.ack(batchId);
                // 处理失败, 回滚数据
                // connector.rollback(batchId);
                //若是 debug模式
                if (log.isDebugEnabled()) {
                    log.debug("确认消息已被消费，消息ID:{}", batchId);
                }
            } catch (CanalClientException e) {
                //每次错误，重试次数减一处理
                log.error("发生错误!! ", e);
                try {
                    //等待时间
                    Thread.sleep(canalProperties.getInterval());
                } catch (InterruptedException e1) {
                }
            }
        }

    }

    /**
     * 处理canal server解析binlog获得的实体类信息
     *
     * @param entrys
     */
    private void dealEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                //开启/关闭事务的实体类型，跳过
                continue;
            }

            //RowChange对象，包含了一行数据变化的所有特征
            //比如
            // isDdl 是否是ddl变更操作
            // sql 具体的ddl sql
            // beforeColumns afterColumns 变更前后的数据字段等等
            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry, e);
            }

            CanalModel canalModel = new CanalModel();

            //获取操作类型：insert/update/delete/truncate类型
            CanalEntry.EventType eventType = rowChage.getEventType();

            //执行头
            CanalEntry.Header entryHeader = entry.getHeader();

            String schemaName = entryHeader.getSchemaName();
            String tableName = entryHeader.getTableName();

            //大写的name
            canalModel.setEventType(eventType.name());
            canalModel.setSchemaName(schemaName);
            canalModel.setTableName(tableName);
            canalModel.setExecuteTime(entryHeader.getExecuteTime());
            canalModel.setSql(rowChage.getSql());

            //打印Header信息
            log.info(String.format("================>> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entryHeader.getLogfileName(),
                    entryHeader.getLogfileOffset(),
                    schemaName,
                    tableName,
                    eventType.name())
            );

            //判断是否是DDL语句
            if (rowChage.getIsDdl()) {
                log.info("================>>isDdl: true,sql: [{}]", rowChage.getSql());
                if (eventType == CanalEntry.EventType.TRUNCATE) {
                    //truncate table xxx;
                    if (canalDataHandler != null) {
                        canalDataHandler.handleTruncate(canalModel);
                    }
                }
            }

            //获取RowChange对象里的每一行数据
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    //如果是删除语句
                    List<CanalColumn> columnList = wrapperCanalModel(rowData.getBeforeColumnsList());
                    canalModel.setBeforeColumns(columnList);

                    //开始处理删除操作
                    if (canalDataHandler != null) {
                        canalDataHandler.handleDelete(canalModel);
                    }
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    //如果是新增语句
                    List<CanalColumn> columnList = wrapperCanalModel(rowData.getAfterColumnsList());
                    canalModel.setAfterColumns(columnList);

                    //开始处理新增操作
                    if (canalDataHandler != null) {
                        canalDataHandler.handleInsert(canalModel);
                    }
                } else {
                    //变更前的数据
                    List<CanalColumn> beforeColumnList = wrapperCanalModel(rowData.getBeforeColumnsList());
                    canalModel.setBeforeColumns(beforeColumnList);
                    //变更后的数据
                    List<CanalColumn> afterColumnList = wrapperCanalModel(rowData.getAfterColumnsList());
                    canalModel.setAfterColumns(afterColumnList);

                    //开始处理更新操作
                    if (canalDataHandler != null) {
                        canalDataHandler.handleUpdate(canalModel);
                    }
                }
            }
        }
    }

    private List<CanalColumn> wrapperCanalModel(List<CanalEntry.Column> columns) {
        List<CanalColumn> columnList = Lists.newArrayList();

        for (CanalEntry.Column column : columns) {
            CanalColumn canalColumn = new CanalColumn();
            canalColumn.setColumnName(column.getName());
            canalColumn.setColumnValue(column.getValue());
            canalColumn.setUpdated(column.getUpdated());
            columnList.add(canalColumn);
        }
        return columnList;
    }


    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
     * <p>This method allows the bean instance to perform validation of its overall
     * configuration and final initialization when all bean properties have been set.
     *
     * @throws Exception in the event of misconfiguration (such as failure to set an
     *                   essential property) or if initialization fails for any other reason
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.run();
    }

    /**
     * Invoked by the containing {@code BeanFactory} on destruction of a bean.
     *
     * @throws Exception in case of shutdown errors. Exceptions will get logged
     *                   but not rethrown to allow other beans to release their resources as well.
     */
    @Override
    public void destroy() throws Exception {
        if (canalConnector != null) {
            canalConnector.disconnect();
        }
    }
}
