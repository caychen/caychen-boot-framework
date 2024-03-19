package com.caychen.boot.common.utils.common;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberToTimeZonesMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.List;
import java.util.Locale;

/**
 * @Author: Caychen
 * @Date: 2021/8/27 11:02
 * @Description:
 */
public class PhoneUtil {

    /**
     * 获取手机归属地（精确到市）
     *
     * @param phoneNum
     * @param language
     * @param locale
     * @return
     */
    public static String getCity(String phoneNum, String language, Locale locale) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();

        PhoneNumberOfflineGeocoder phoneNumberOfflineGeocoder = PhoneNumberOfflineGeocoder.getInstance();

        Phonenumber.PhoneNumber referencePhonenumber = null;

        try {
            referencePhonenumber = instance.parse(phoneNum, language);
            //手机号码归属城市 city
            String city = phoneNumberOfflineGeocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);

            return city;
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取手机的运营商
     *
     * @param phoneNum
     * @param language
     * @param locale
     * @return
     */
    public static String getCarrier(String phoneNum, String language, Locale locale) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();

        PhoneNumberToCarrierMapper phoneNumberToCarrierMapper = PhoneNumberToCarrierMapper.getInstance();

        try {
            Phonenumber.PhoneNumber referencePhonenumber = instance.parse(phoneNum, language);

            //手机运营商
            String carrier = phoneNumberToCarrierMapper.getNameForNumber(referencePhonenumber, locale);
            return carrier;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取手机的时区
     *
     * @param phoneNum
     * @param language
     * @return
     */
    public static List<String> getTimeZone(String phoneNum, String language) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();

        PhoneNumberToTimeZonesMapper phoneNumberToTimeZonesMapper = PhoneNumberToTimeZonesMapper.getInstance();

        try {
            Phonenumber.PhoneNumber referencePhonenumber = instance.parse(phoneNum, language);

            //手机运营商
            List<String> zoneList = phoneNumberToTimeZonesMapper.getTimeZonesForNumber(referencePhonenumber);
            return zoneList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean validPhone(String phoneNum, String language) {
        PhoneNumberUtil instance = PhoneNumberUtil.getInstance();

        try {
            Phonenumber.PhoneNumber referencePhonenumber = instance.parse(phoneNum, language);

            //校验手机号
            return instance.isValidNumber(referencePhonenumber);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
}
