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

import br.com.uol.pagseguro.properties.PagSeguroConfig;

/**
 * Class Log implements Logger
 */
public class Log implements Logger {

    /**
     * Log4j Após adicionar o log4j na sua aplicação, descomente as seguintes linhas: private org.apache.log4j.Logger
     * log; log = org.apache.log4j.Logger.getLogger(c);
     * 
     * E remova ou comente as seguintes linhas: private LogDefault log; log = new LogDefault();
     * 
     */

    private final LogDefault log;

    // private final org.apache.log4j.Logger log;

    /**
     * Construct
     * 
     * @param c
     */
    public Log(Class<?> c) {
        // log = org.apache.log4j.Logger.getLogger(c);
        log = new LogDefault();
    }

    /**
     * Logger Debug
     */
    public void debug(String message) {
        if (validLog()) {
            log.debug(message);
        }
    }

    /**
     * Logger info
     */
    public void info(String message) {
        if (validLog()) {
            log.info(message);
        }
    }

    /**
     * Logger Warn
     */
    public void warn(String message) {
        if (validLog()) {
            log.warn(message);
        }
    }

    /**
     * Logger Error
     */
    public void error(String message) {
        if (validLog()) {
            log.error(message);
        }
    }

    /**
     * Logger Warn
     */
    public void warn(String message, Throwable t) {
        if (validLog()) {
            log.warn(message, t);
        }
    }

    /**
     * Logger Error
     */
    public void error(String message, Throwable t) {
        if (validLog()) {
            log.error(message, t);
        }
    }

    /**
     * @return boolean
     */
    private boolean validLog() {
        return PagSeguroConfig.getLogActive();
    }

}
