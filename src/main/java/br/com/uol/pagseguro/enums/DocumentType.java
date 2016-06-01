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

package br.com.uol.pagseguro.enums;

/**
 * Enum constants that represents the senderDocument type accepted by PagSeguro on payment requests
 */
public enum DocumentType {

    CPF("Cadastro de Pessoa Física"),

    CNPJ("Cadastro de Pessoa Jurídica");

    private String description;

    /**
     * Initializes a newly created enum constant of this type with the specified argument
     * 
     * @param description
     *            - the description of the enum constant
     */
    DocumentType(String description) {
        this.description = description;
    }

    /**
     * @return this enum constant description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     *            - new description for this enum constant
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
