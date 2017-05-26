import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyWalletSharedModule } from '../../shared';
import { MyWalletAdminModule } from '../../admin/admin.module';
import {
    UserAccountService,
    UserAccountPopupService,
    UserAccountComponent,
    UserAccountDetailComponent,
    UserAccountDialogComponent,
    UserAccountPopupComponent,
    UserAccountDeletePopupComponent,
    UserAccountDeleteDialogComponent,
    userAccountRoute,
    userAccountPopupRoute,
    UserAccountResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...userAccountRoute,
    ...userAccountPopupRoute,
];

@NgModule({
    imports: [
        MyWalletSharedModule,
        MyWalletAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserAccountComponent,
        UserAccountDetailComponent,
        UserAccountDialogComponent,
        UserAccountDeleteDialogComponent,
        UserAccountPopupComponent,
        UserAccountDeletePopupComponent,
    ],
    entryComponents: [
        UserAccountComponent,
        UserAccountDialogComponent,
        UserAccountPopupComponent,
        UserAccountDeleteDialogComponent,
        UserAccountDeletePopupComponent,
    ],
    providers: [
        UserAccountService,
        UserAccountPopupService,
        UserAccountResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyWalletUserAccountModule {}
