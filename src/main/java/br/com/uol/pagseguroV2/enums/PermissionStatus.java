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

package br.com.uol.pagseguroV2.enums;

/**
 * Enum constants that represents a list of known permission status
 */
public enum PermissionStatus {

	PENDING ("PENDING"),
	APPROVED ("APPROVED"),
	DENIED ("DENIED");

	private String value;

	private PermissionStatus(String value) {
		this.value = value;
	}

    public String getValue() {
        return value;
    }

    public static PermissionStatus fromValue(String value) {

        for (PermissionStatus permissionStatus : values()) {
            if (permissionStatus.value.equals(value)) {
                return permissionStatus;
            }
        }
		return null;
    }

    @Override
    public String toString() {
    	return value;
    }

}
