package com.example.Demo.Repository;
import com.example.Demo.Model.OrphanageImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrphanageImageRepository extends MongoRepository<OrphanageImage, String> {

    List<OrphanageImage> findOrphanageImageByOrphanageId(String orphanageId);

    void deleteByOrphanageIdAndId(String orphanageId, String id);
}
