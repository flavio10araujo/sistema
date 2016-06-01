package br.com.uol.pagseguro.domain;

import br.com.uol.pagseguro.enums.MetaDataItemKey;
import br.com.uol.pagseguro.helper.PagSeguroUtil;

public class MetaDataItem {

    private MetaDataItemKey key;

    private String value;

    private Integer group;

    /**
     * Initializes a newly created object of this type
     * 
     */
    public MetaDataItem() {
    }

    /**
     * Initializes a newly created object of this type with the specified arguments
     * 
     * @param key
     *            - the metaDataItemKey of the object
     * @param value
     *            - the metaDataItemValue of the object
     */
    public MetaDataItem(MetaDataItemKey key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Initializes a newly created object of this type with the specified arguments
     * 
     * @param key
     *            - the metaDataItemKey of the object
     * @param value
     *            - the metaDataItemValue of the object
     * @param group
     *            - the metaDataItemGroup of the object
     */
    public MetaDataItem(MetaDataItemKey key, String value, Integer group) {
        this.setKey(key);
        this.setValue(value);
        this.setGroup(group);
    }

    /**
     * Normalize a given metaDataItemValue
     * 
     * @param value
     *            - the metaDataItemValue of the object
     */
    private String normalizeValue(String value) {

        if (this.getKey().equals(MetaDataItemKey.PASSENGER_CPF)) {
            value = PagSeguroUtil.getOnlyNumbers(value);
        }

        if (this.getKey().equals(MetaDataItemKey.TIME_IN_GAME_DAYS)) {
            value = PagSeguroUtil.getOnlyNumbers(value);
        }

        return value;
    }

    /**
     * @return this MetaDataItem key
     */
    public MetaDataItemKey getKey() {
        return this.key;
    }

    /**
     * @param key
     *            - new key for this MetaDataItem
     */
    public void setKey(MetaDataItemKey key) {
        this.key = key;
    }

    /**
     * @return this MetaDataItem value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     *            - new normalized value for this MetaDataItem
     */
    public void setValue(String value) {
        this.value = normalizeValue(value);
    }

    /**
     * @return this MetaDataItem group
     */
    public Integer getGroup() {
        return this.group;
    }

    /**
     * @param group
     *            - new group for this MetaDataItem
     */
    public void setGroup(Integer group) {
        this.group = group;
    }
}
