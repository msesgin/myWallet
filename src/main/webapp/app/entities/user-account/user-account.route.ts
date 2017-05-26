import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { UserAccountComponent } from './user-account.component';
import { UserAccountDetailComponent } from './user-account-detail.component';
import { UserAccountPopupComponent } from './user-account-dialog.component';
import { UserAccountDeletePopupComponent } from './user-account-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class UserAccountResolvePagingParams implements Resolve<any> {

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

export const userAccountRoute: Routes = [
    {
        path: 'user-account',
        component: UserAccountComponent,
        resolve: {
            'pagingParams': UserAccountResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-account/:id',
        component: UserAccountDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userAccount.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userAccountPopupRoute: Routes = [
    {
        path: 'user-account-new',
        component: UserAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-account/:id/edit',
        component: UserAccountPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-account/:id/delete',
        component: UserAccountDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userAccount.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
