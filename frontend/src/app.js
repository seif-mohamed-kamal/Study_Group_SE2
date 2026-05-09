// API base URL
const API_BASE = 'http://localhost:8085/api';

// Global state store
document.addEventListener('alpine:init', () => {
  Alpine.store('auth', {
    token: localStorage.getItem('sg_token') || '',
    roles: JSON.parse(localStorage.getItem('sg_roles') || '[]').map(r => typeof r === 'string' ? r.toLowerCase() : r),
    email: localStorage.getItem('sg_email') || '',

    set(token, roles, email) {
      this.token = token;
      this.roles = (roles || []).map(r => typeof r === 'string' ? r.toLowerCase() : r);
      this.email = email;
      localStorage.setItem('sg_token', token);
      localStorage.setItem('sg_roles', JSON.stringify(this.roles));
      localStorage.setItem('sg_email', email);
    },

    clear() {
      this.token = '';
      this.roles = [];
      this.email = '';
      localStorage.removeItem('sg_token');
      localStorage.removeItem('sg_roles');
      localStorage.removeItem('sg_email');
    }
  });

  Alpine.store('groupAccess', {
    allowed: new Set(),
    loading: false,

    async refresh() {
      const auth = Alpine.store('auth');
      if (!auth.token) {
        this.allowed = new Set();
        return;
      }
      this.loading = true;
      try {
        this.allowed = await getAllowedGroupIds(auth.roles);
      } catch {
        this.allowed = new Set();
      } finally {
        this.loading = false;
      }
    },

    canAccess(groupId) {
      const auth = Alpine.store('auth');
      if (auth.roles.includes('admin')) return true;
      return this.allowed.has(groupId);
    }
  });
});

// API helper functions
async function apiRequest(endpoint, options = {}) {
  const auth = Alpine.store('auth');
  const token = auth.token;
  const headers = {
    'Content-Type': 'application/json',
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers
  };

  const response = await fetch(`${API_BASE}${endpoint}`, {
    ...options,
    headers
  });

  const result = await response.json().catch(() => ({}));

  // Unwrap ResponseDTO wrapper (works for both success and error responses that carry data)
  if (result && typeof result === 'object' && 'data' in result) {
    if (!response.ok && result.data === null) {
      throw result;
    }
    return result.data;
  }

  if (!response.ok) {
    throw result;
  }
  return result;
}

// Authentication functions
async function login(email, password) {
  const response = await apiRequest('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password })
  });
  return response;
}

async function register(data) {
  const response = await apiRequest('/auth/register', {
    method: 'POST',
    body: JSON.stringify(data)
  });
  return response;
}

// Group functions
async function getGroups() {
  const response = await apiRequest('/groups/getAll');
  return response;
}

async function getGroupById(id) {
  const response = await apiRequest(`/groups/${id}`);
  return response;
}

async function getAllowedGroupIds(roles) {
  if (roles.includes('admin')) return new Set();
  if (roles.includes('creator')) {
    const response = await apiRequest('/group-creator/groups');
    return new Set(response.map(g => g.id));
  }
  if (roles.includes('student')) {
    const response = await apiRequest('/student/myGroups');
    return new Set(response.map(g => g.id));
  }
  return new Set();
}

// Main app component
function app() {
  return {
    currentRoute: '/',

    init() {
      Alpine.store('groupAccess').refresh();
      this.handleRoute();
      window.addEventListener('hashchange', () => this.handleRoute());
    },

    handleRoute() {
      const hash = window.location.hash.slice(1) || '/';
      this.currentRoute = hash;
      const auth = Alpine.store('auth');
      
      // Protected route checks
      if (this.currentRoute.startsWith('/student') && !auth.token) {
        window.location.hash = '/login';
        return;
      }
      if (this.currentRoute.startsWith('/creator') && !auth.token) {
        window.location.hash = '/login';
        return;
      }
      if (this.currentRoute === '/admin' && !auth.token) {
        window.location.hash = '/login';
        return;
      }
    },

    logout() {
      Alpine.store('auth').clear();
      Alpine.store('groupAccess').allowed = new Set();
      window.location.hash = '/';
    },

    get auth() {
      return Alpine.store('auth');
    },

    get groupAccess() {
      return Alpine.store('groupAccess');
    },

    canAccessGroup(groupId) {
      return Alpine.store('groupAccess').canAccess(groupId);
    }
  };
}

// Page components
function homePage() {
  return {
    groups: [],
    searchMode: 'subject',
    textQuery: '',
    fromTime: '',
    toTime: '',
    status: '',
    isSuccess: false,

    async init() {
      await this.loadGroups();
    },

    async loadGroups() {
      try {
        this.groups = await getGroups();
      } catch {
        this.groups = [];
      }
    },

    async runSearch() {
      this.status = '';
      this.isSuccess = false;
      try {
        if (this.searchMode === 'subject') {
          if (!this.textQuery.trim()) return await this.loadGroups();
          const response = await apiRequest(`/groups/subject/${encodeURIComponent(this.textQuery)}`);
          this.groups = response;
          return;
        }
        if (this.searchMode === 'location') {
          if (!this.textQuery.trim()) return await this.loadGroups();
          const response = await apiRequest(`/groups/location/${encodeURIComponent(this.textQuery)}`);
          this.groups = response;
          return;
        }
        const params = new URLSearchParams();
        if (this.fromTime) params.append('from', new Date(this.fromTime).toISOString());
        if (this.toTime) params.append('to', new Date(this.toTime).toISOString());
        const response = await apiRequest(`/groups/by-time?${params.toString()}`);
        this.groups = response;
      } catch (err) {
        this.status = err.message || 'Search failed.';
        this.isSuccess = false;
      }
    },

    async joinGroup(groupId) {
      const auth = Alpine.store('auth');
      if (!auth.token) {
        this.status = 'Please login first.';
        this.isSuccess = false;
        return;
      }
      try {
        const response = await apiRequest(`/student/joinGroup/${groupId}`, {
          method: 'POST'
        });
        this.status = response || 'Join request sent.';
        this.isSuccess = true;
        // Refresh group access
        Alpine.store('groupAccess').refresh();
      } catch (err) {
        this.status = err.message || 'Join failed.';
        this.isSuccess = false;
      }
    }
  };
}

function loginPage() {
  return {
    email: '',
    password: '',
    error: '',

    async onSubmit() {
      this.error = '';
      try {
        console.log('Attempting login with:', this.email);
        const result = await login(this.email, this.password);
        console.log('Login result:', result);

        if (!result) {
          throw new Error('Login failed: Invalid response from server');
        }
        
        const success = result.success ?? result.Success ?? true;
        if (!success) {
          throw new Error(result.message || result.Message || 'Login failed.');
        }

        const payload = result.data || result;
        const token = payload.token || payload.Token;
        const roles = payload.roles || payload.Roles || [];
        const email = payload.email || payload.Email;

        console.log('Extracted data:', { token, roles, email });

        if (!token) {
          throw new Error('No token received from server');
        }

        Alpine.store('auth').set(token, roles, email);
        Alpine.store('groupAccess').refresh();

        if (roles.includes('admin')) {
          window.location.hash = '/admin';
        } else if (roles.includes('creator')) {
          window.location.hash = '/creator';
        } else if (roles.includes('student')) {
          window.location.hash = '/student';
        } else {
          window.location.hash = '/';
        }
      } catch (err) {
        console.error('Login error:', err);
        this.error = err.message || 'Login failed.';
      }
    }
  };
}

function registerPage() {
  return {
    form: {
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
      role: 'STUDENT'
    },
    status: '',

    async onSubmit() {
      this.status = '';
      if (this.form.password !== this.form.confirmPassword) {
        this.status = 'Passwords do not match.';
        return;
      }
      try {
        await register(this.form);
        if (this.form.role === 'CREATOR') {
          this.status = 'Creator registration submitted. After an admin approves your account, you can sign in.';
        } else {
          this.status = 'Registered successfully. You can now login.';
        }
        window.location.hash = '/login';
      } catch (err) {
        this.status = err.message || 'Register failed.';
      }
    }
  };
}

function adminPage() {
  return {
    creatorRequests: [],
    groupRequests: [],
    notice: null,

    async init() {
      await this.loadCreatorRequests();
      await this.loadGroupRequests();
    },

    async loadCreatorRequests() {
      try {
        const response = await apiRequest('/admin/creator/requests');
        this.creatorRequests = response || [];
      } catch {
        this.creatorRequests = [];
      }
    },

    async loadGroupRequests() {
      try {
        const response = await apiRequest('/admin/group/requests');
        this.groupRequests = response || [];
      } catch {
        this.groupRequests = [];
      }
    },

    async decideCreator(userId, accepted) {
      this.notice = null;
      try {
        await apiRequest(accepted ? `/admin/approve/account/${userId}` : `/admin/reject/account/${userId}`, {
          method: 'POST'
        });
        this.creatorRequests = this.creatorRequests.filter(r => r.UserId !== userId);
        this.notice = {
          kind: accepted ? 'success' : 'muted',
          text: accepted ? 'Creator account approved.' : 'Creator account rejected.'
        };
      } catch (err) {
        this.notice = { kind: 'error', text: err.message || 'Request failed.' };
      }
    },

    async decideGroup(groupId, accepted) {
      this.notice = null;
      try {
        await apiRequest(accepted ? `/admin/accept/group/${groupId}` : `/admin/reject/group/${groupId}`, {
          method: 'POST'
        });
        this.groupRequests = this.groupRequests.filter(g => g.id !== groupId);
        this.notice = {
          kind: accepted ? 'success' : 'muted',
          text: accepted ? 'Group request approved.' : 'Group request rejected.'
        };
      } catch (err) {
        this.notice = { kind: 'error', text: err.message || 'Request failed.' };
      }
    }
  };
}

function creatorPage() {
  return {
    groups: [],
    requests: [],
    status: '',

    async init() {
      await this.loadGroups();
      await this.loadRequests();
    },

    async loadGroups() {
      try {
        const response = await apiRequest('/group-creator/groups');
        console.log('Creator groups response:', response);
        this.groups = response || [];
      } catch (err) {
        console.error('Creator groups fetch error:', err);
        this.status = err.message || 'Failed to load your groups.';
        this.groups = [];
      }
    },

    async loadRequests() {
      try {
        const response = await apiRequest('/group-creator/joinRequests');
        this.requests = response || [];
      } catch (err) {
        console.error('Creator requests fetch error:', err);
        this.requests = [];
      }
    },

    async decide(requestId, accepted) {
      this.status = '';
      try {
        await apiRequest(accepted ? `/group-creator/accept/request/${requestId}` : `/group-creator/reject/request/${requestId}`, {
          method: 'POST'
        });
        this.requests = this.requests.filter(r => r.RequestId !== requestId);
        this.status = accepted ? 'Request accepted.' : 'Request rejected.';
      } catch (err) {
        this.status = err.message || 'Request failed.';
      }
    },

    async deleteGroup(groupId) {
      this.status = '';
      try {
        const response = await apiRequest(`/groups/${groupId}`, {
          method: 'DELETE'
        });
        const msg = (response && response.message) || (response && response.Message);
        this.status = msg || 'Deleted successfully.';
        await this.loadGroups();
      } catch (err) {
        this.status = err.message || 'Delete failed.';
      }
    }
  };
}

function studentPage() {
  return {
    requests: [],
    status: '',

    async init() {
      await this.loadRequests();
    },

    async loadRequests() {
      try {
        console.log('Loading student requests...');
        const response = await apiRequest('/student/myRequests');
        console.log('Student requests response:', response);
        this.requests = response || [];
        console.log('Student requests loaded:', this.requests.length);
      } catch (err) {
        console.error('Failed to load student requests:', err);
        this.status = err.message || 'Failed to load requests.';
        this.requests = [];
      }
    }
  };
}

function groupDetailsPage() {
  return {
    group: null,
    status: '',
    
    async init() {
      const groupId = window.location.hash.split('/')[2];
      try {
        this.group = await getGroupById(groupId);
      } catch (err) {
        this.status = err.message || 'Failed to load group.';
      }
    }
  };
}

function creatorCreateGroupPage() {
  return {
    form: {
      subject: '',
      location: 'Online',
      meetingType: 'InPerson',
      description: '',
      maxMembers: 2,
      meetingTime: ''
    },
    status: '',

    async onSubmit() {
      this.status = '';
      try {
        const payload = {
          ...this.form,
          maxMembers: parseInt(this.form.maxMembers) || 2,
          meetingTime: this.form.meetingTime ? new Date(this.form.meetingTime).toISOString() : null
        };
        const response = await apiRequest('/groups', {
          method: 'POST',
          body: JSON.stringify(payload)
        });
        this.status = (response && response.message) || (response && response.Message) || 'Group created successfully.';
        window.location.hash = '/creator';
      } catch (err) {
        this.status = err.message || 'Failed to create group.';
      }
    }
  };
}

function creatorEditGroupPage() {
  return {
    form: {
      subject: '',
      location: '',
      meetingType: 'InPerson',
      description: '',
      maxMembers: 2,
      meetingTime: ''
    },
    status: '',
    
    async init() {
      const groupId = window.location.hash.split('/')[3];
      try {
        const group = await getGroupById(groupId);
        this.form = {
          subject: group.subject,
          location: group.location,
          meetingType: group.meetingType,
          description: group.description,
          maxMembers: group.maxMembers || 2,
          meetingTime: group.meetingTime ? new Date(group.meetingTime).toISOString().slice(0, 16) : ''
        };
      } catch (err) {
        this.status = err.message || 'Failed to load group.';
      }
    },

    async onSubmit() {
      this.status = '';
      const groupId = window.location.hash.split('/')[3];
      try {
        const payload = {
          ...this.form,
          maxMembers: parseInt(this.form.maxMembers) || 2,
          meetingTime: this.form.meetingTime ? new Date(this.form.meetingTime).toISOString() : null
        };
        const response = await apiRequest(`/groups/${groupId}`, {
          method: 'PUT',
          body: JSON.stringify(payload)
        });
        this.status = (response && response.message) || (response && response.Message) || 'Group updated successfully.';
        window.location.hash = '/creator';
      } catch (err) {
        this.status = err.message || 'Failed to update group.';
      }
    }
  };
}

function materialsPage() {
  return {
    materials: [],
    comments: [],
    activeMaterialId: null,
    newComment: '',
    status: '',

    async init() {
      const groupId = window.location.hash.split('/')[2];
      try {
        const response = await apiRequest(`/material/group/${groupId}`);
        this.materials = response || [];
      } catch (err) {
        this.status = err.message || 'Failed to load materials.';
      }
    },

    async loadComments(materialId) {
      this.activeMaterialId = materialId;
      this.comments = [];
      this.status = '';
      try {
        const response = await apiRequest(`/comment/${materialId}`);
        this.comments = response || [];
      } catch (err) {
        this.status = err.message || 'Failed to load comments.';
      }
    },

    async addComment(materialId) {
      if (!this.newComment.trim()) return;
      this.status = '';
      try {
        await apiRequest('/comment', {
          method: 'POST',
          body: JSON.stringify({
            materialId: parseInt(materialId),
            text: this.newComment.trim()
          })
        });
        this.newComment = '';
        await this.loadComments(materialId);
      } catch (err) {
        this.status = err.message || 'Failed to add comment.';
      }
    }
  };
}

function materialsAddPage() {
  return {
    form: {
      title: '',
      content: '',
      fileType: 'Link',
      fileUrl: ''
    },
    status: '',
    
    async onSubmit() {
      this.status = '';
      const groupId = window.location.hash.split('/')[2];
      try {
        const response = await apiRequest('/material/add', {
          method: 'POST',
          body: JSON.stringify({
            groupId: parseInt(groupId),
            description: this.form.content,
            fileName: this.form.title,
            filePath: this.form.fileUrl
          })
        });
        this.status = response.message || response.Message || 'Material added successfully.';
        window.location.hash = `/materials/${groupId}`;
      } catch (err) {
        this.status = err.message || 'Failed to add material.';
      }
    }
  };
}
