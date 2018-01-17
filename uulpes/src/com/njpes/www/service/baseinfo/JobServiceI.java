package com.njpes.www.service.baseinfo;

import java.util.Set;

public interface JobServiceI {

    public Set<Long> findAncestorIds(Iterable<Long> currentIds);

    public Set<Long> findAncestorIds(Long currentId);
}
