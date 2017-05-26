import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { AccountTransactions } from './account-transactions.model';
import { AccountTransactionsPopupService } from './account-transactions-popup.service';
import { AccountTransactionsService } from './account-transactions.service';
import { User, UserService } from '../../shared';
import { UserAccount, UserAccountService } from '../user-account';

@Component({
    selector: 'jhi-account-transactions-dialog',
    templateUrl: './account-transactions-dialog.component.html'
})
export class AccountTransactionsDialogComponent implements OnInit {

    accountTransactions: AccountTransactions;
    authorities: any[];
    isSaving: boolean;

    tousers: User[];

    fromuseraccounts: UserAccount[];

    touseraccounts: UserAccount[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private accountTransactionsService: AccountTransactionsService,
        private userService: UserService,
        private userAccountService: UserAccountService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.tousers = res.json(); }, (res: Response) => this.onError(res.json()));
        this.userAccountService.queryByCurrentUser().subscribe(
            (res: Response) => { this.fromuseraccounts = res.json(); }, (res: Response) => this.onError(res.json()));
      }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.accountTransactions.id !== undefined) {
            this.subscribeToSaveResponse(
                this.accountTransactionsService.update(this.accountTransactions));
        } else {
            this.subscribeToSaveResponse(
                this.accountTransactionsService.create(this.accountTransactions));
        }
    }

    getToAccountsByUserId(){
        this.touseraccounts = null;
        this.userAccountService.queryByUser(this.accountTransactions.toUserEmailId).subscribe(
       (res: Response) => { this.touseraccounts = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    
    private subscribeToSaveResponse(result: Observable<AccountTransactions>) {
        result.subscribe((res: AccountTransactions) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AccountTransactions) {
        this.eventManager.broadcast({ name: 'accountTransactionsListModification', content: 'OK'});
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

    trackToUserById(index: number, item: User) {
        return item.id;
    }

    trackFromUserAccountById(index: number, item: UserAccount) {
        return item.id;
    }
    
    trackFromToAccountById(index: number, item: UserAccount) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-account-transactions-popup',
    template: ''
})
export class AccountTransactionsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private accountTransactionsPopupService: AccountTransactionsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.accountTransactionsPopupService
                    .open(AccountTransactionsDialogComponent, params['id']);
            } else {
                this.modalRef = this.accountTransactionsPopupService
                    .open(AccountTransactionsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
