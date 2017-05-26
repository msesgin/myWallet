import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { MyWalletTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CurrencyDetailComponent } from '../../../../../../main/webapp/app/entities/currency/currency-detail.component';
import { CurrencyService } from '../../../../../../main/webapp/app/entities/currency/currency.service';
import { Currency } from '../../../../../../main/webapp/app/entities/currency/currency.model';

describe('Component Tests', () => {

    describe('Currency Management Detail Component', () => {
        let comp: CurrencyDetailComponent;
        let fixture: ComponentFixture<CurrencyDetailComponent>;
        let service: CurrencyService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MyWalletTestModule],
                declarations: [CurrencyDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CurrencyService,
                    EventManager
                ]
            }).overrideComponent(CurrencyDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CurrencyDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CurrencyService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Currency(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.currency).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
