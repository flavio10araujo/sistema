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
 * Represents a metaDataItem in a transaction
 */
public class MetaData {

    private List<MetaDataItem> items;

    /**
     * Construct Default
     */
    public MetaData() {

    }

    /**
     * Construct
     * 
     * @param items
     */
    public MetaData(List<MetaDataItem> items) {
        if (!items.isEmpty()) {
            this.items = items;
        }
    }

    /**
     * @param metaDataItem
     *            - add a new item on the end of this list
     */
    public void addItem(MetaDataItem metaDataItem) {
        this.getItem().add(metaDataItem);
    }

    /**
     * @param items
     *            - new item list for this MetaDataItem
     */
    public void setItems(List<MetaDataItem> items) {
        this.items = items;
    }

    /**
     * @return a list of MetaDataItems
     */
    public List<MetaDataItem> getItem() {
        if (this.items == null) {
            this.items = new ArrayList<MetaDataItem>();
        }

        return this.items;
    }

}
