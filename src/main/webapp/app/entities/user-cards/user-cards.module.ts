import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MyWalletSharedModule } from '../../shared';
import { MyWalletAdminModule } from '../../admin/admin.module';
import {
    UserCardsService,
    UserCardsPopupService,
    UserCardsComponent,
    UserCardsDetailComponent,
    UserCardsDialogComponent,
    UserCardsPopupComponent,
    UserCardsDeletePopupComponent,
    UserCardsDeleteDialogComponent,
    userCardsRoute,
    userCardsPopupRoute,
    UserCardsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...userCardsRoute,
    ...userCardsPopupRoute,
];

@NgModule({
    imports: [
        MyWalletSharedModule,
        MyWalletAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        UserCardsComponent,
        UserCardsDetailComponent,
        UserCardsDialogComponent,
        UserCardsDeleteDialogComponent,
        UserCardsPopupComponent,
        UserCardsDeletePopupComponent,
    ],
    entryComponents: [
        UserCardsComponent,
        UserCardsDialogComponent,
        UserCardsPopupComponent,
        UserCardsDeleteDialogComponent,
        UserCardsDeletePopupComponent,
    ],
    providers: [
        UserCardsService,
        UserCardsPopupService,
        UserCardsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyWalletUserCardsModule {}
