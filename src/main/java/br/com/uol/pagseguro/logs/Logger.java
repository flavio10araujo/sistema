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
package br.com.uol.pagseguro.logs;

/**
 * 
 * Interface Logger
 */
public interface Logger {

    /**
     * Logger Debug
     * 
     * @param message
     */
    void debug(String message);

    /**
     * Logger Info
     * 
     * @param message
     */
    void info(String message);

    /**
     * Logger Warn
     * 
     * @param message
     */
    void warn(String message);

    /**
     * Logger error
     * 
     * @param message
     */
    void error(String message);

    /**
     * Logger Warn
     * 
     * @param message
     * @param t
     */
    void warn(String message, Throwable t);

    /**
     * Logger Error
     * 
     * @param message
     * @param t
     */
    void error(String message, Throwable t);

}
