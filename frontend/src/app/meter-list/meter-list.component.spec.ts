import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MeterListComponent } from './meter-list.component';

describe('MeterListComponent', () => {
  let component: MeterListComponent;
  let fixture: ComponentFixture<MeterListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MeterListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MeterListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
