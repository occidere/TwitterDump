package org.occidere.twitterdump.repository;

import org.occidere.twitterdump.dto.TwitterPhoto;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author occidere
 * @since 2019-03-02
 * Blog: https://blog.naver.com/occidere
 * Github: https://github.com/occidere
 */
public interface TwitterRepository extends MongoRepository<TwitterPhoto, String> {
}
