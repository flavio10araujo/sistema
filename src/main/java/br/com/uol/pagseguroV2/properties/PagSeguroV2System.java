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
package br.com.uol.pagseguroV2.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Helper to get values from properties
 */
@Component
public class PagSeguroV2System {

    private static String pagSeguroCheckoutServiceServicePath;
    private static String pagSeguroEnvironmentProductionWebserviceUrl;
    private static String pagSeguroEnvironmentSandboxWebserviceUrl;
    @Getter private static String pagSeguroPaymentServiceNfDescription;
    @Getter private static String pagSeguroEncoding;
    private static String pagSeguroServiceTimeout;

    private PagSeguroV2System() {
    }

    @Value("${pagSeguro.checkoutService.v2.servicePath}")
    public void setPagSeguroCheckoutServiceServicePath(String pagSeguroCheckoutServiceServicePath) {
        PagSeguroV2System.pagSeguroCheckoutServiceServicePath = pagSeguroCheckoutServiceServicePath;
    }

    @Value("${pagSeguro.environment.production.v2.webserviceUrl}")
    public void setPagSeguroEnvironmentProductionWebserviceUrl(String pagSeguroEnvironmentProductionWebserviceUrl) {
        PagSeguroV2System.pagSeguroEnvironmentProductionWebserviceUrl = pagSeguroEnvironmentProductionWebserviceUrl;
    }

    @Value("${pagSeguro.environment.sandbox.v2.webserviceUrl}")
    public void setPagSeguroEnvironmentSandboxWebserviceUrl(String pagSeguroEnvironmentSandboxWebserviceUrl) {
        PagSeguroV2System.pagSeguroEnvironmentSandboxWebserviceUrl = pagSeguroEnvironmentSandboxWebserviceUrl;
    }

    @Value("${pagSeguro.paymentService.nf.description}")
    public void setPagSeguroPaymentServiceNfDescription(String pagSeguroPaymentServiceNfDescription) {
        PagSeguroV2System.pagSeguroPaymentServiceNfDescription = pagSeguroPaymentServiceNfDescription;
    }

    @Value("${pagSeguro.encoding}")
    public void setPagSeguroEncoding(String pagSeguroEncoding) {
        PagSeguroV2System.pagSeguroEncoding = pagSeguroEncoding;
    }

    @Value("${pagSeguro.serviceTimeout}")
    public void setPagSeguroServiceTimeout(String pagSeguroServiceTimeout) {
        PagSeguroV2System.pagSeguroServiceTimeout = pagSeguroServiceTimeout;
    }

    private static boolean isProduction() {
        return "production".equals(PagSeguroV2Config.getEnvironment());
    }

    public static String getUrlProduction() {
        if (isProduction()) {
            return pagSeguroEnvironmentProductionWebserviceUrl;
        }

        return pagSeguroEnvironmentSandboxWebserviceUrl;
    }

    public static String getCheckoutServicePath() {
        return pagSeguroCheckoutServiceServicePath;
    }

    public static String getServiceTimeout() {
        return pagSeguroServiceTimeout;
    }

    public static String getCompleteCheckoutServicePath() {
        if (isProduction()) {
            return pagSeguroEnvironmentProductionWebserviceUrl + pagSeguroCheckoutServiceServicePath;
        }

        return pagSeguroEnvironmentSandboxWebserviceUrl + pagSeguroCheckoutServiceServicePath;
    }
}
