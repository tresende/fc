package com.tresende.catalog.admin.application.castmember.retreive.get;

import com.tresende.catalog.admin.application.UseCase;

sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
