package br.com.uol.pagseguro.service.authorization;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.uol.pagseguro.domain.Authorization;
import br.com.uol.pagseguro.domain.Credentials;
import br.com.uol.pagseguro.domain.Error;
import br.com.uol.pagseguro.enums.HttpStatus;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.logs.Log;
import br.com.uol.pagseguro.parser.authorization.AuthorizationParser;
import br.com.uol.pagseguro.service.ConnectionData;
import br.com.uol.pagseguro.service.TransactionSearchService;
import br.com.uol.pagseguro.utils.HttpConnection;
import br.com.uol.pagseguro.xmlparser.ErrorsParser;

public class AuthorizationSearchService {
	
	public AuthorizationSearchService(){
	}
	
	/**
     * PagSeguro Log tool
     * 
     * @see Log
     */
    private static Log log = new Log(TransactionSearchService.class);
    
    /**
     * @var DATE_FORMAT
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm"; // "2011-04-01T08:30"
    
    /**
     * @var String
     */
    private static final String SEARCH_BY_CODE = "AuthorizationSearchService.SearchByCode(authorizationCode= %1s) - error %2s";
    
    /**
     * @var String
     */
    private static final String SEARCH_BY_CODE_BEGIN = "AuthorizationSearchService.SearchByCode(authorizationCode= %s) - begin";

    /**
     * @var String
     */
    private static final String SEARCH_BY_DATE = "AuthorizationSearchService.SearchAll() - error %s";
    
    /**
     * @var String
     */
    private static final String SEARCH_BY_DATE_BEGIN = "AuthorizationSearchService.SearchAll() - begin";
    
    /**
     * Build Search Url By Code
     * 
     * @param connectionData
     * @param authorizationCode
     * @return
     * @throws PagSeguroServiceException
     */
    private static String buildSearchUrlByCode(ConnectionData connectionData, String authorizationCode)
            throws PagSeguroServiceException {
        return connectionData.getWSAuthorizationFindByCodeUrl() + authorizationCode + "?" + connectionData.getCredentialsUrlQuery();
    }
    
    /**
     * Build Search byDate
     * 
     * @param connectionData
     * @param initialDate 
     * @param finalDate
     * @param pageNumber
     * @param resultsPerPage
     * @return
     * @throws PagSeguroServiceException
     */
    private static String buildSearchUrlByDate(ConnectionData connectionData, String initialDate, String finalDate, Integer pageNumber, Integer resultsPerPage)
            throws PagSeguroServiceException {
    	
    	StringBuilder sb = new StringBuilder();
        sb.append(connectionData.getWSAuthorizationFindByDateUrl() + "?" + connectionData.getCredentialsUrlQuery());
        sb.append("&initialDate=" + (initialDate != null ? initialDate : ""));
        sb.append("&finalDate=" + (finalDate != null ? finalDate : ""));

        if (pageNumber != null) {
            sb.append("&page=" + pageNumber);
        }

        if (resultsPerPage != null) {
            sb.append("&maxPageResults=" + resultsPerPage);
        }
    	
        return sb.toString();
    }
    
    /**
     * searchAuthorizationsByCode
     * 
     * @param credentials
     * @param authorizationCode
     * @throws Exception
     */
    public static Authorization searchByCode(Credentials credentials, String authorizationCode)
            throws PagSeguroServiceException {
    	
    	AuthorizationSearchService.log.info(String.format(AuthorizationSearchService.SEARCH_BY_CODE_BEGIN, authorizationCode));

    	Authorization authorization = null;
    	
        ConnectionData connectionData = new ConnectionData(credentials);
        String authorizationSearchURL = AuthorizationSearchService.buildSearchUrlByCode(connectionData, authorizationCode);
        
        HttpURLConnection response = AuthorizationSearchService.searchAuthorization(credentials, authorizationSearchURL, 
        		connectionData.getServiceTimeout(), connectionData.getCharset(), AuthorizationSearchService.SEARCH_BY_CODE);
        
        try {
        	
        	authorization = AuthorizationParser.readAuthorization(response.getInputStream());
        	
		} catch (Exception e) {
			throw new PagSeguroServiceException("Parser error", e);
		} finally {
			response.disconnect();
		}
        
        AuthorizationSearchService.log.info(String.format(AuthorizationSearchService.SEARCH_BY_CODE, authorizationCode,
                authorization.toString()));
        
        return authorization;
        
    } 
    
    /**
     * searchByDate
     * 
     * @param credentials
     * @param authorizationCode
     * @throws Exception
     */
    public static List<Authorization> searchByDate(Credentials credentials, Date initialDate, Date finalDate, int pageNumber, int resultsPerPage) throws PagSeguroServiceException {
    	
    	AuthorizationSearchService.log.info(String.format(AuthorizationSearchService.SEARCH_BY_DATE_BEGIN));
    	
    	String dtInitial = AuthorizationSearchService.formatDate(initialDate);
        String dtFinal = AuthorizationSearchService.formatDate(finalDate);

    	List<Authorization> authorizations = null;
    	
        ConnectionData connectionData = new ConnectionData(credentials);
        String authorizationSearchURL = AuthorizationSearchService.buildSearchUrlByDate(connectionData, dtInitial, dtFinal, pageNumber, resultsPerPage);
        
        HttpURLConnection response = AuthorizationSearchService.searchAuthorization(credentials, authorizationSearchURL, 
        		connectionData.getServiceTimeout(), connectionData.getCharset(), AuthorizationSearchService.SEARCH_BY_DATE);
        
        try {
        	
        	authorizations = AuthorizationParser.readAuthorizations(response.getInputStream());
        	
		} catch (Exception e) {
			throw new PagSeguroServiceException("Parser error", e);
		} finally {
			response.disconnect();
		}
        
        AuthorizationSearchService.log.info(String.format(AuthorizationSearchService.SEARCH_BY_DATE, authorizations.toString()));
        
        return authorizations;
        
    } 
    
    /**
     * searchAuthorization
     * 
     * @param credentials
     * @param notificationURL
     * @param logInfo
     * @throws Exception
     */
    public static HttpURLConnection searchAuthorization(Credentials credentials, String authorizationSearchURL,
    		String serviceTimeout, String charset, String logInfo)
            throws PagSeguroServiceException {

        HttpConnection connection = new HttpConnection();
        HttpStatus httpCodeStatus = null;

        HttpURLConnection response = connection.get(authorizationSearchURL, serviceTimeout, charset, null);

        try {

            httpCodeStatus = HttpStatus.fromCode(response.getResponseCode());

            if (HttpURLConnection.HTTP_OK == httpCodeStatus.getCode().intValue()) {

               return response;

            } else if (HttpURLConnection.HTTP_BAD_REQUEST == httpCodeStatus.getCode().intValue()) {

                List<Error> errors = ErrorsParser.readErrosXml(response.getErrorStream());

                PagSeguroServiceException exception = new PagSeguroServiceException(httpCodeStatus, errors);

                AuthorizationSearchService.log.error(String.format(logInfo, exception.getMessage()));

                throw exception;
            } else {
                throw new PagSeguroServiceException(httpCodeStatus);
            }

        } catch (PagSeguroServiceException e) {
            throw e;
        } catch (Exception e) {

        	AuthorizationSearchService.log.error(String.format(logInfo, e.getMessage()));

            throw new PagSeguroServiceException(httpCodeStatus, e);

        }
    
    }
    
    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(AuthorizationSearchService.DATE_FORMAT);
        return sdf.format(date);
    }    

}
