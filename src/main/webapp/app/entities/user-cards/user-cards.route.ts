import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { UserCardsComponent } from './user-cards.component';
import { UserCardsDetailComponent } from './user-cards-detail.component';
import { UserCardsPopupComponent } from './user-cards-dialog.component';
import { UserCardsDeletePopupComponent } from './user-cards-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class UserCardsResolvePagingParams implements Resolve<any> {

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

export const userCardsRoute: Routes = [
    {
        path: 'user-cards',
        component: UserCardsComponent,
        resolve: {
            'pagingParams': UserCardsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userCards.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-cards/:id',
        component: UserCardsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userCards.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userCardsPopupRoute: Routes = [
    {
        path: 'user-cards-new',
        component: UserCardsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userCards.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-cards/:id/edit',
        component: UserCardsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userCards.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-cards/:id/delete',
        component: UserCardsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'myWalletApp.userCards.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
