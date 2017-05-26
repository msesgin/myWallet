import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyWalletSharedModule } from '../../shared';
import { MyWalletAdminModule } from '../../admin/admin.module';
import {
    AccountTransactionsService,
    AccountTransactionsPopupService,
    AccountTransactionsComponent,
    AccountTransactionsDetailComponent,
    AccountTransactionsDialogComponent,
    AccountTransactionsPopupComponent,
    AccountTransactionsDeletePopupComponent,
    AccountTransactionsDeleteDialogComponent,
    accountTransactionsRoute,
    accountTransactionsPopupRoute,
    AccountTransactionsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...accountTransactionsRoute,
    ...accountTransactionsPopupRoute,
];

@NgModule({
    imports: [
        MyWalletSharedModule,
        MyWalletAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AccountTransactionsComponent,
        AccountTransactionsDetailComponent,
        AccountTransactionsDialogComponent,
        AccountTransactionsDeleteDialogComponent,
        AccountTransactionsPopupComponent,
        AccountTransactionsDeletePopupComponent,
    ],
    entryComponents: [
        AccountTransactionsComponent,
        AccountTransactionsDialogComponent,
        AccountTransactionsPopupComponent,
        AccountTransactionsDeleteDialogComponent,
        AccountTransactionsDeletePopupComponent,
    ],
    providers: [
        AccountTransactionsService,
        AccountTransactionsPopupService,
        AccountTransactionsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyWalletAccountTransactionsModule {}
