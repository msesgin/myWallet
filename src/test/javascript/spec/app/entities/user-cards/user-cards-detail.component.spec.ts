import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { MyWalletTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { UserCardsDetailComponent } from '../../../../../../main/webapp/app/entities/user-cards/user-cards-detail.component';
import { UserCardsService } from '../../../../../../main/webapp/app/entities/user-cards/user-cards.service';
import { UserCards } from '../../../../../../main/webapp/app/entities/user-cards/user-cards.model';

describe('Component Tests', () => {

    describe('UserCards Management Detail Component', () => {
        let comp: UserCardsDetailComponent;
        let fixture: ComponentFixture<UserCardsDetailComponent>;
        let service: UserCardsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MyWalletTestModule],
                declarations: [UserCardsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    UserCardsService,
                    EventManager
                ]
            }).overrideComponent(UserCardsDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(UserCardsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(UserCardsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new UserCards(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.userCards).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
