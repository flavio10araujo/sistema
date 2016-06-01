package br.com.uol.pagseguro.domain.authorization;

import java.util.ArrayList;
import java.util.List;

import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.authorization.AuthorizationService;

/**
 * Represents a Authorization request
 */
public class AuthorizationRequest {

	/**
	 * Reference code
	 * 
	 * Optional. You can use the reference code to store an identifier so you can associate the PagSeguro authorization to
	 * a authorization in your system.
	 */
	private String reference;
    
    /**
     * List of permissions in this authorization
     */
	private List<String> permissions;
    
    /**
	 * Uri to where the PagSeguro checkout page should redirect the user after the payment information is processed.
     * Typically this is a confirmation page on your web site.
     */
    private String redirectURL;
   
    /**
     * Determines for which url PagSeguro will send the order related notifications changes.
     * 
     * Optional. A new notification will be send to this url if any change happens in the transaction status. You can
     * use that for update the related order.
     */
	private String notificationURL;
	
	public AuthorizationRequest() {
		this.permissions = new ArrayList<String>();
	}

	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public void addPermission(String permission){
		if (permission != null || !"".equals(permission)) {
			this.permissions.add(permission);
		}
	}
	
	public String getRedirectURL() {
		return redirectURL;
	}
	
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}
	
	public String getNotificationURL() {
		return notificationURL;
	}
	
	public void setNotificationURL(String notificationURL) {
		this.notificationURL = notificationURL;
	}
	
	/**
     * Calls the PagSeguro web service and register this request for authorization
     * 
     * @param credentials
     * @return The URL to where the user needs to be redirected to in order to complete the authorization process
     * @throws PagSeguroServiceException
     */
    public String register(Credentials credentials) throws PagSeguroServiceException {
        return this.register(credentials, false);
    }

    /**
     * Calls the PagSeguro web service and register this request for authorization
     * 
     * @param credentials
     * @param onlyAuthorizationCode
     * @return The authorization code
     * @throws PagSeguroServiceException
     */
    public String register(Credentials credentials, Boolean onlyAuthorizationCode) throws PagSeguroServiceException {
        return AuthorizationService.createAuthorizationRequest(credentials, this, onlyAuthorizationCode);
    }

	@Override
	public String toString() {
		return "Authorization [reference=" + reference + ", permissions="
				+ permissions + ", redirectURL=" + redirectURL
				+ ", notificationURL=" + notificationURL + "]";
	}

}
