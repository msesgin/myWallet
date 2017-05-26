import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { AccountTransactionsComponent } from './account-transactions.component';
import { AccountTransactionsDetailComponent } from './account-transactions-detail.component';
import { AccountTransactionsPopupComponent } from './account-transactions-dialog.component';
import { AccountTransactionsDeletePopupComponent } from './account-transactions-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class AccountTransactionsResolvePagingParams implements Resolve<any> {

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

export const accountTransactionsRoute: Routes = [
    {
        path: 'account-transactions',
        component: AccountTransactionsComponent,
        resolve: {
            'pagingParams': AccountTransactionsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.accountTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'account-transactions/:id',
        component: AccountTransactionsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.accountTransactions.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const accountTransactionsPopupRoute: Routes = [
    {
        path: 'account-transactions-new',
        component: AccountTransactionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.accountTransactions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'account-transactions/:id/edit',
        component: AccountTransactionsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.accountTransactions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'account-transactions/:id/delete',
        component: AccountTransactionsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.accountTransactions.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
