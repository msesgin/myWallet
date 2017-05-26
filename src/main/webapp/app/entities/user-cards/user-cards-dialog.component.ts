import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { UserCards } from './user-cards.model';
import { UserCardsPopupService } from './user-cards-popup.service';
import { UserCardsService } from './user-cards.service';
import { User, UserService } from '../../shared';
import { UserAccount, UserAccountService } from '../user-account';

@Component({
    selector: 'jhi-user-cards-dialog',
    templateUrl: './user-cards-dialog.component.html'
})
export class UserCardsDialogComponent implements OnInit {

    userCards: UserCards;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    useraccounts: UserAccount[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private userCardsService: UserCardsService,
        private userService: UserService,
        private userAccountService: UserAccountService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));
        this.userAccountService.query().subscribe(
            (res: Response) => { this.useraccounts = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.userCards.id !== undefined) {
            this.subscribeToSaveResponse(
                this.userCardsService.update(this.userCards));
        } else {
            this.subscribeToSaveResponse(
                this.userCardsService.create(this.userCards));
        }
    }

    private subscribeToSaveResponse(result: Observable<UserCards>) {
        result.subscribe((res: UserCards) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: UserCards) {
        this.eventManager.broadcast({ name: 'userCardsListModification', content: 'OK'});
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

    trackUserAccountById(index: number, item: UserAccount) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-user-cards-popup',
    template: ''
})
export class UserCardsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userCardsPopupService: UserCardsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.userCardsPopupService
                    .open(UserCardsDialogComponent, params['id']);
            } else {
                this.modalRef = this.userCardsPopupService
                    .open(UserCardsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
