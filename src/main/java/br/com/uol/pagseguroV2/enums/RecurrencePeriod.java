/*
 ************************************************************************
 Copyright [2014] [PagSeguro Internet Ltda.]

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

package br.com.uol.pagseguroV2.enums;

public enum RecurrencePeriod {
    WEEKLY('W'), MONTHLY('M'), YEARLY('Y');

    private final Character id;

    private RecurrencePeriod(Character id) {
        this.id = id;
    }

    public Character getId() {
        return this.id;
    }

    public static RecurrencePeriod fromValue(Character id) {
        for (RecurrencePeriod value : RecurrencePeriod.values()) {
            if (value.getId().equals(id)) {
                return value;
            }
        }

        return null;
    }
}
