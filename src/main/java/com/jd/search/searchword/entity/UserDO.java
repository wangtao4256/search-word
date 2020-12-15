package com.jd.search.searchword.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("user")
public class UserDO {
    @Id
    private ObjectId id;

    private String name;
}
