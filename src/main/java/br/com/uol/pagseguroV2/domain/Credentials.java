/*
 * ***********************************************************************
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
 * ***********************************************************************
 */

package br.com.uol.pagseguroV2.domain;

import br.com.uol.pagseguroV2.exception.PagSeguroServiceException;

import java.util.Map;

/**
 * Abstract class that represents a PagSeguro credential
 */
public abstract class Credentials {

    /**
     * @return a map of name value pairs that compose this set of credentials
     */
    public abstract Map<Object, Object> getAttributes() throws PagSeguroServiceException;

    /**
     * @return a string that represents the current object
     */
    @Override
    public abstract String toString();
}
