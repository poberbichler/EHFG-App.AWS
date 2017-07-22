package org.ehfg.app.twitter

/**
 * @author patrick
 * @since 06.2017
 */
interface TwitterService {
    boolean addListener(String hashtag)

    boolean removeListener(String hasthag)

    Collection<String> getListener()
}