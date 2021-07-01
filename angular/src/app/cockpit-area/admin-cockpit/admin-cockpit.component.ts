// Admin Cockpit
// From here, users with the role 'admin' can navigate users and archived orders

// Imports
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { TranslocoService } from '@ngneat/transloco';
import { ConfigService } from 'app/core/config/config.service';
import { FilterAdmin, Pageable } from 'app/shared/backend-models/filters';
import { Sort } from '@angular/material/sort';
import { UserListView, UserRole } from 'app/shared/view-models/interfaces';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { AdminUserManageComponent } from '../admin-cockpit/admin-user-manage/admin-user-manage.component';
import { AdminCockpitService } from '../services/admin-cockpit.service';
import { AdminUserAddComponent } from './admin-user-add/admin-user-add.component';
import { Title } from '@angular/platform-browser';
@Component({
  selector: 'app-admin-cockpit',
  templateUrl: './admin-cockpit.component.html',
  styleUrls: ['./admin-cockpit.component.scss'],
})

// class
export class AdminCockpitComponent implements OnInit {
  private translocoSubscription = Subscription.EMPTY;
  userRoles: UserRole[];
  private element: ElementRef;
  private pageable: Pageable = {
    pageSize: 8,
    pageNumber: 0,
  };

  private sorting: { property: any; direction: any }[] = [];

  pageSize = 8;

  @ViewChild('pagingBar', { static: true }) pagingBar: MatPaginator;

  users: UserListView[] = [];
  totalUsers: number;

  columns: { name: String; label: any }[];

  // columns displayed in view of current users
  displayedColumns: string[] = ['username', 'email', 'userRole', 'editButton'];

  pageSizes: number[];

  // filter options for view of current users
  filters: FilterAdmin = {
    username: undefined,
    email: undefined,
    userRoleId: undefined,
  };

  constructor(
    private dialog: MatDialog,
    private translocoService: TranslocoService,
    private adminCockpitService: AdminCockpitService,
    private configService: ConfigService,
    private _element: ElementRef,
    private title: Title,
  ) {
    this.title.setTitle('Admin');
    this.element = _element;
    this.pageSizes = this.configService.getValues().pageSizes;
    this.getUserRoles();
  }

  // On Initialization, user Roles are reveived from the backend and filters (if given) are applied to users
  // Texts are translated accoding to language of current user
  ngOnInit(): void {
    //this.getUserRoles;
    this.applyFilters();
    this.translocoService.langChanges$.subscribe((event: any) => {
      this.setTableHeaders(event);
      moment.locale(this.translocoService.getActiveLang());
    });
  }

  setTableHeaders(lang: string): void {
    this.translocoSubscription = this.translocoService
      .selectTranslateObject('admin', {}, lang)
      .subscribe((admin) => {
        this.columns = [
          { name: 'username', label: admin.usernameH },
          { name: 'email', label: admin.emailH },
          { name: 'date', label: admin.registrationDateH },
          { name: 'role', label: admin.roleH },
          { name: 'editButton', label: admin.editButtonH },
          { name: 'noSelection', label: admin.noSelectionH },
        ];
      });
  }

  applyFilters(): void {
    // Converts the selected booking date from inline edit component to a isoString so that it's matching the format of the backend
    if (this.filters.bookingDate != undefined) {
      this.filters.bookingDate = new Date(
        new Date(this.filters.bookingDate).setHours(12),
      ).toISOString();
    }
    const tempFilterUserRole = this.filters.userRoleId;
    if (this.filters.userRoleId) {
      this.filterUserRole(this.filters.userRoleId);
    }
    this.adminCockpitService
      .getUsers(this.pageable, this.sorting, this.filters)
      .subscribe((data: any) => {
        const userRoles: UserRole[] = this.userRoles;
        if (!data) {
          this.users = [];
        } else {
          this.users = data.content;
        }
        this.totalUsers = data.totalElements;
      });
    this.filters.userRoleId = tempFilterUserRole;
  }

  // Clears all filters entered into the filter area
  clearFilters(filters: any): void {
    filters.reset();
    this.applyFilters();
    this.pagingBar.firstPage();
  }

  // Called when the user selects a user role by which to sort the table
  filterUserRole(event) {
    this.userRoles.forEach((userRole) => {
      if (JSON.stringify(userRole.name) == JSON.stringify(event)) {
        this.filters.userRoleId = userRole.id;
      }
    });
  }

  page(pagingEvent: PageEvent): void {
    this.pageable = {
      pageSize: pagingEvent.pageSize,
      pageNumber: pagingEvent.pageIndex,
      sort: this.pageable.sort,
    };
    this.applyFilters();
  }

  // Sorts the table by whichever criteria has been selected
  sort(sortEvent: Sort): void {
    this.sorting = [];
    if (sortEvent.direction) {
      this.sorting.push({
        property: sortEvent.active,
        direction: '' + sortEvent.direction,
      });
    }
    this.applyFilters();
  }

  // Opens the user-manage component for the selected user
  selected(selection: UserListView): void {
    let openendDialog = this.dialog.open(AdminUserManageComponent, {
      width: '940px',
      height: '575px',
      data: selection,
    });
    openendDialog.afterClosed().subscribe((result: any) => {
      setTimeout(() => this.applyFilters(), 1000);
      this.title.setTitle('Admin');
    });
  }

  // TODO : Close AddUser-Window after register
  selectedUser(): void {
    let openendDialog = this.dialog.open(AdminUserAddComponent, {
      //width: '800px',
      height: '700px',
      width: '600px',
    });

    openendDialog.afterClosed().subscribe((result: any) => {
      setTimeout(() => this.applyFilters(), 1000);
      this.title.setTitle('Admin');
    });
  }

  closeDialog(): void {
    this.dialog.closeAll;
  }

  ngOnDestroy(): void {
    this.translocoSubscription.unsubscribe();
  }

  // Gets all user roles from the backend for the filter area dropdown
  getUserRoles() {
    this.pageable.sort = [];
    this.adminCockpitService
      .getUserRoles(this.pageable)
      .subscribe((data: any) => {
        if (!data) {
          console.log('no data found in users');
        }
        {
          this.userRoles = data.content;
          console.log(data.content);
        }
      });
  }
}
