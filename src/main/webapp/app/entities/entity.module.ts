import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MyWalletCurrencyModule } from './currency/currency.module';
import { MyWalletUserAccountModule } from './user-account/user-account.module';
import { MyWalletUserCardsModule } from './user-cards/user-cards.module';
import { MyWalletAccountTransactionsModule } from './account-transactions/account-transactions.module';
import { MyWalletCardTransactionsModule } from './card-transactions/card-transactions.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        MyWalletCurrencyModule,
        MyWalletUserAccountModule,
        MyWalletUserCardsModule,
        MyWalletAccountTransactionsModule,
        MyWalletCardTransactionsModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MyWalletEntityModule {}
