import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { UserAccount } from './user-account.model';
import { UserAccountPopupService } from './user-account-popup.service';
import { UserAccountService } from './user-account.service';
import { User, UserService } from '../../shared';
import { Currency, CurrencyService } from '../currency';

@Component({
    selector: 'jhi-user-account-dialog',
    templateUrl: './user-account-dialog.component.html'
})
export class UserAccountDialogComponent implements OnInit {

    userAccount: UserAccount;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    currencies: Currency[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private userAccountService: UserAccountService,
        private userService: UserService,
        private currencyService: CurrencyService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));
        this.currencyService.query().subscribe(
            (res: Response) => { this.currencies = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userAccount.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userAccountService.update(this.userAccount));
        } else {
            this.subscribeToSaveResponse(
                this.userAccountService.create(this.userAccount));
        }
    }

    private subscribeToSaveResponse(result: Observable<UserAccount>) {
        result.subscribe((res: UserAccount) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: UserAccount) {
        this.eventManager.broadcast({ name: 'userAccountListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackCurrencyById(index: number, item: Currency) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-user-account-popup',
    template: ''
})
export class UserAccountPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userAccountPopupService: UserAccountPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.userAccountPopupService
                    .open(UserAccountDialogComponent, params['id']);
            } else {
                this.modalRef = this.userAccountPopupService
                    .open(UserAccountDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
