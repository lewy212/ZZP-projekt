package com.zawadzkia.springtodo.task.category;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskCategoryDTO implements Serializable {
    private Long id;

    private String name;

    private String description;

    private String image;
}
