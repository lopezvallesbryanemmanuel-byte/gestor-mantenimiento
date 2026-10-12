import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MantenimientoList } from './mantenimiento-list';

describe('MantenimientoList', () => {
  let component: MantenimientoList;
  let fixture: ComponentFixture<MantenimientoList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MantenimientoList],
    }).compileComponents();

    fixture = TestBed.createComponent(MantenimientoList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
