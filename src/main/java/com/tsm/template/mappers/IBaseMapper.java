package com.tsm.template.mappers;

import com.tsm.template.model.BaseModel;

import java.util.Set;

public interface IBaseMapper<R, M extends BaseModel> {

    M toModel(final R dto);

    R toDTO(final M model);

    Set<R> toDTOs(final Set<M> models);
}
