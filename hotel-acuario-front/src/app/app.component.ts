import { Component, inject, AfterViewInit, OnDestroy } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NgIf, AsyncPipe } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import Swal from 'sweetalert2';

import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    NgIf,
    AsyncPipe,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatButtonModule,
    MatMenuModule,
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements AfterViewInit, OnDestroy {
  title = 'hotel-acuario-front';
  authService = inject(AuthService);
  router = inject(Router);
  private observer?: MutationObserver;

  get currentUser$() {
    return this.authService.currentUser$;
  }

  get isAuthenticated() {
    return this.authService.isAuthenticated();
  }

  hasRole(role: string): boolean {
    return this.authService.hasRole(role);
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  ngAfterViewInit(): void {
    this.observer = new MutationObserver(() => {
      this.fixMenuPosition();
    });

    const overlayContainer = document.querySelector('.cdk-overlay-container');
    if (overlayContainer) {
      this.observer.observe(overlayContainer, {
        childList: true,
        subtree: true,
        attributes: true,
        attributeFilter: ['style', 'class']
      });
    }

    setTimeout(() => this.fixMenuPosition(), 50);
    setTimeout(() => this.fixMenuPosition(), 150);
    setTimeout(() => this.fixMenuPosition(), 300);
  }

  ngOnDestroy(): void {
    if (this.observer) {
      this.observer.disconnect();
    }
  }

  private fixMenuPosition(): void {
    const menuPanels = document.querySelectorAll('.cdk-overlay-pane');
    menuPanels.forEach((panel: Element) => {
      const htmlPanel = panel as HTMLElement;
      if (htmlPanel.querySelector('.user-menu-container')) {
        const trigger = document.querySelector('.user-menu-trigger') as HTMLElement;
        if (trigger) {
          const rect = trigger.getBoundingClientRect();
          htmlPanel.style.position = 'fixed';
          htmlPanel.style.right = '16px';
          htmlPanel.style.left = 'auto';
          htmlPanel.style.top = `${rect.bottom + 4}px`;
          htmlPanel.style.transform = 'none';
          htmlPanel.style.maxWidth = '280px';
          htmlPanel.style.width = '280px';
          htmlPanel.style.overflowX = 'hidden';
          htmlPanel.style.overflowY = 'hidden';
          htmlPanel.style.marginLeft = '0';
          htmlPanel.style.marginRight = '0';
        }
        
        const menuContent = htmlPanel.querySelector('.mat-mdc-menu-content') as HTMLElement;
        if (menuContent) {
          menuContent.style.overflowX = 'hidden';
          menuContent.style.overflowY = 'hidden';
          menuContent.style.maxWidth = '280px';
          menuContent.style.width = '280px';
        }
        
        const menuPanel = htmlPanel.querySelector('.mat-mdc-menu-panel') as HTMLElement;
        if (menuPanel) {
          menuPanel.style.overflowX = 'hidden';
          menuPanel.style.overflowY = 'hidden';
          menuPanel.style.maxWidth = '280px';
          menuPanel.style.width = '280px';
        }
      }
    });
  }

  logout(): void {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Está seguro que desea cerrar sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.authService.logout();
        this.router.navigate(['/login']);
        Swal.fire({
          icon: 'success',
          title: 'Sesión cerrada',
          text: 'Ha cerrado sesión correctamente',
          timer: 2000,
          timerProgressBar: true,
          showConfirmButton: false
        });
      }
    });
  }
}
