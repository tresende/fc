package com.tresende.catalog.admin.application.castmember.retreive.get;

import com.tresende.catalog.admin.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase {
}
