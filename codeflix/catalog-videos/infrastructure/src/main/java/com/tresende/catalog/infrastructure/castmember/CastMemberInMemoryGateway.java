package com.tresende.catalog.infrastructure.castmember;

import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import com.tresende.catalog.domain.castmember.CastMemberSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CastMemberInMemoryGateway implements CastMemberGateway {

    private final Map<String, CastMember> db;

    public CastMemberInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public CastMember save(final CastMember aMember) {
        return db.put(aMember.id(), aMember);
    }

    @Override
    public void deleteById(final String anId) {
        db.remove(anId);
    }

    @Override
    public Optional<CastMember> findById(final String anId) {
        return Optional.ofNullable(db.get(anId));
    }

    @Override
    public Pagination<CastMember> findAll(final CastMemberSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                db.size(),
                db.values().stream().toList()
        );
    }
}
