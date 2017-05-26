import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyWalletSharedModule } from '../../shared';
import {
    CardTransactionsService,
    CardTransactionsPopupService,
    CardTransactionsComponent,
    CardTransactionsDetailComponent,
    CardTransactionsDialogComponent,
    CardTransactionsPopupComponent,
    CardTransactionsDeletePopupComponent,
    CardTransactionsDeleteDialogComponent,
    cardTransactionsRoute,
    cardTransactionsPopupRoute,
    CardTransactionsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cardTransactionsRoute,
    ...cardTransactionsPopupRoute,
];

@NgModule({
    imports: [
        MyWalletSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CardTransactionsComponent,
        CardTransactionsDetailComponent,
        CardTransactionsDialogComponent,
        CardTransactionsDeleteDialogComponent,
        CardTransactionsPopupComponent,
        CardTransactionsDeletePopupComponent,
    ],
    entryComponents: [
        CardTransactionsComponent,
        CardTransactionsDialogComponent,
        CardTransactionsPopupComponent,
        CardTransactionsDeleteDialogComponent,
        CardTransactionsDeletePopupComponent,
    ],
    providers: [
        CardTransactionsService,
        CardTransactionsPopupService,
        CardTransactionsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyWalletCardTransactionsModule {}
