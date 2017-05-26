import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CardTransactionsComponent } from './card-transactions.component';
import { CardTransactionsDetailComponent } from './card-transactions-detail.component';
import { CardTransactionsPopupComponent } from './card-transactions-dialog.component';
import { CardTransactionsDeletePopupComponent } from './card-transactions-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CardTransactionsResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: PaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const cardTransactionsRoute: Routes = [
    {
        path: 'card-transactions',
        component: CardTransactionsComponent,
        resolve: {
            'pagingParams': CardTransactionsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.cardTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'card-transactions/:id',
        component: CardTransactionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.cardTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cardTransactionsPopupRoute: Routes = [
    {
        path: 'card-transactions-new',
        component: CardTransactionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.cardTransactions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'card-transactions/:id/edit',
        component: CardTransactionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.cardTransactions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'card-transactions/:id/delete',
        component: CardTransactionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.cardTransactions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
