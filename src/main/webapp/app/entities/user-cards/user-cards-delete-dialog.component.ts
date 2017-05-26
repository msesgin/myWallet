import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { UserCards } from './user-cards.model';
import { UserCardsPopupService } from './user-cards-popup.service';
import { UserCardsService } from './user-cards.service';

@Component({
    selector: 'jhi-user-cards-delete-dialog',
    templateUrl: './user-cards-delete-dialog.component.html'
})
export class UserCardsDeleteDialogComponent {

    userCards: UserCards;

    constructor(
        private userCardsService: UserCardsService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userCardsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userCardsListModification',
                content: 'Deleted an userCards'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-cards-delete-popup',
    template: ''
})
export class UserCardsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userCardsPopupService: UserCardsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.userCardsPopupService
                .open(UserCardsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
