import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EquipoDetail } from './equipo-detail';

describe('EquipoDetail', () => {
  let component: EquipoDetail;
  let fixture: ComponentFixture<EquipoDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EquipoDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(EquipoDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
