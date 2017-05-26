import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UserCards } from './user-cards.model';
import { UserCardsService } from './user-cards.service';
@Injectable()
export class UserCardsPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private userCardsService: UserCardsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.userCardsService.find(id).subscribe((userCards) => {
                this.userCardsModalRef(component, userCards);
            });
        } else {
            return this.userCardsModalRef(component, new UserCards());
        }
    }

    userCardsModalRef(component: Component, userCards: UserCards): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.userCards = userCards;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
