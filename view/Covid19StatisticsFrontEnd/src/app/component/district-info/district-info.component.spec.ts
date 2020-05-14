import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DistrictInfoComponent } from './district-info.component';

describe('DistrictInfoComponent', () => {
  let component: DistrictInfoComponent;
  let fixture: ComponentFixture<DistrictInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DistrictInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DistrictInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
