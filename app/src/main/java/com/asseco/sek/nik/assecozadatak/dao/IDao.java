package com.asseco.sek.nik.assecozadatak.dao;

/**
 * Interface for data storage. DAO stores and reads hashes.
 * Each hash is uniquely identified with URL.
 *
 * Created by sekul on 21.12.2015..
 */
public interface IDao {

    /**
     * Stores hash.
     *
     * @param url url
     * @param hash hash
     * @throws DaoException if error occurs
     */
    void storeHash(String url, String hash) throws DaoException;

    /**
     * Gets hash from storage.
     * @param url url
     * @return hash taht was  storeed with provided url. null if there is no hash woth provided Url.
     * @throws DaoException if error occurs
     */
    String getHash(String url) throws DaoException;

    /**
     * Call this method when dao is not needed any more.
     * Frees any resource that dao consumes.
     */
    void close();
}
