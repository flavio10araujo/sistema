/*
 ************************************************************************
 Copyright [2011] [PagSeguro Internet Ltda.]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 ************************************************************************
 */

package br.com.uol.pagseguro.domain;

/**
 * 
 * Represent a parameter item
 */
public class ParameterItem {

    /**
     * String name
     */
    private String name;

    /**
     * String value
     */
    private String value;

    /**
     * Integer group
     */
    private Integer index;

    /**
     * Construct Default
     */
    public ParameterItem() {
    }

    /**
     * Construct ParameterItem
     * 
     * @param name
     * @param value
     */
    public ParameterItem(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Construct ParameterItem
     * 
     * @param name
     * @param value
     * @param index
     */
    public ParameterItem(String name, String value, Integer index) {
        this.name = name;
        this.value = value;
        this.index = index;
    }

    /**
     * Get Key
     * 
     * @return key
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set Key
     * 
     * @param key
     */
    public void setName(String key) {
        this.name = key;
    }

    /**
     * Get Value
     * 
     * @return value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set Value
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get Group
     * 
     * @return group
     */
    public Integer getIndex() {
        return this.index;
    }

    /**
     * Set Group
     * 
     * @param index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

}
