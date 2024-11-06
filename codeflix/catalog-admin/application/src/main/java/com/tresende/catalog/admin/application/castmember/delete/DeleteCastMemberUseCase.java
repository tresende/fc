package com.tresende.catalog.admin.application.castmember.delete;

import com.tresende.catalog.admin.application.UnitUseCase;


public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase {
}