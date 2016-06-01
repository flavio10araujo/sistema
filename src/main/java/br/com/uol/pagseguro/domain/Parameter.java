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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Represent a parameter
 */
public class Parameter {

    /**
     * List of ParameterItem
     */
    private List<ParameterItem> items;

    /**
     * Construct Default
     */
    public Parameter() {
    }

    /**
     * Construct Parameter
     * 
     * @param items
     */
    public Parameter(List<ParameterItem> items) {
        if (!items.isEmpty()) {
            this.items = items;
        }
    }

    /**
     * Add Item
     * 
     * @param parameterItem
     */
    public void addItem(ParameterItem parameterItem) {
        this.getItems().add(parameterItem);
    }

    /**
     * Get Items
     * 
     * @return list ParameterItem
     */
    public List<ParameterItem> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<ParameterItem>();
        }

        return this.items;
    }

    /**
     * Set Items
     * 
     * @param items
     */
    public void setItems(List<ParameterItem> items) {
        this.items = items;
    }
}
