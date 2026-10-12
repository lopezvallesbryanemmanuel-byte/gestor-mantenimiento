import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';

import { AuthService } from './core/auth/auth.service';
import { Mensajes } from './shared/mensajes/mensajes';
import { Navbar } from './shared/navbar/navbar';

@Component({
  imports: [RouterOutlet, Navbar, Mensajes],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {
  protected readonly auth = inject(AuthService);
}
