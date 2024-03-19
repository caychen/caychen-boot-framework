package com.caychen.boot.elasticsearch.utils;

import com.alibaba.fastjson.JSON;
import com.caychen.boot.common.enums.ErrorEnum;
import com.caychen.boot.common.exception.BusinessException;
import com.caychen.boot.elasticsearch.config.ElasticsearchConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: Caychen
 * @Date: 2021/9/27 13:45
 * @Description:
 */
@Slf4j
@Component
public class EsUtil {

    /**
     * 关键字
     */
    public static final String KEYWORD = ".keyword";
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     *
     * @param index 索引
     * @return
     */
    public Boolean createIndex(String index) throws IOException {
        if (isExistIndex(index)) {
            throw new BusinessException(ErrorEnum.ES_INDEX_HAS_FOUND_ERROR, "索引：[" + index + "]");
        }
        //1.创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(index);
        //2.执行客户端请求
        CreateIndexResponse response = restHighLevelClient.indices().create(request, ElasticsearchConfig.DEFAULT_OPTIONS);

        log.info("创建索引{}成功", index);

        return response.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param index
     * @return
     */
    public Boolean deleteIndex(String index) throws IOException {
        if (!isExistIndex(index)) {
            throw new BusinessException(ErrorEnum.ES_INDEX_NOT_FOUND_ERROR, "索引：[" + index + "]");
        }
        //删除索引请求
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        //执行客户端请求
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(request, ElasticsearchConfig.DEFAULT_OPTIONS);

        log.info("删除索引{}成功", index);

        return delete.isAcknowledged();
    }


    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public Boolean isExistIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        Boolean exists = restHighLevelClient.indices().exists(request, ElasticsearchConfig.DEFAULT_OPTIONS);
        return exists;
    }

    /**
     * 创建数据
     *
     * @param index 索引
     * @param id    数据id，如果存在，则更新；如果不存在，则新增
     * @param obj
     * @return
     * @throws IOException
     */
    public Boolean saveOrUpdate(String index, String id, Object obj) throws IOException {
        if (!isExistIndex(index)) {
            throw new BusinessException(ErrorEnum.ES_INDEX_NOT_FOUND_ERROR, "索引：[" + index + "]");
        }
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(id);
        indexRequest.source(JSON.toJSONString(obj), XContentType.JSON);
        log.info("执行脚本：[{}]", indexRequest.toString());

        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, ElasticsearchConfig.DEFAULT_OPTIONS);
        RestStatus status = indexResponse.status();
        return status.getStatus() == RestStatus.CREATED.getStatus();
    }

    /**
     * 全量更新
     *
     * @param index
     * @param id
     * @param obj
     * @return
     * @throws IOException
     */
    public Boolean updateData(String index, String id, Object obj) throws IOException {
        if (!isExistIndex(index)) {
            throw new BusinessException(ErrorEnum.ES_INDEX_NOT_FOUND_ERROR, "索引：[" + index + "]");
        }

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index);
        updateRequest.id(id);

        //设置文档
        updateRequest.doc(JSON.toJSONString(obj), XContentType.JSON);

        log.info("执行脚本：[{}]", updateRequest.toString());
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, ElasticsearchConfig.DEFAULT_OPTIONS);

        return true;
    }

}
