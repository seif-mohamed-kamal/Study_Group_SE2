import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext.jsx";
import { GroupAccessProvider, GroupMemberGuard } from "./context/GroupAccessContext.jsx";
import { Protected } from "./components/Protected.jsx";
import { HomePage } from "./pages/HomePage.jsx";
import { LoginPage } from "./pages/LoginPage.jsx";
import { RegisterPage } from "./pages/RegisterPage.jsx";
import { GroupDetailsPage } from "./pages/GroupDetailsPage.jsx";
import { StudentPage } from "./pages/StudentPage.jsx";
import { CreatorPage } from "./pages/CreatorPage.jsx";
import { CreatorCreateGroupPage } from "./pages/CreatorCreateGroupPage.jsx";
import { CreatorEditGroupPage } from "./pages/CreatorEditGroupPage.jsx";
import { AdminPage } from "./pages/AdminPage.jsx";
import { MaterialsPage } from "./pages/MaterialsPage.jsx";
import { MaterialsAddPage } from "./pages/MaterialsAddPage.jsx";

export function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <GroupAccessProvider>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/group/:id" element={<GroupDetailsPage />} />

            <Route path="/student" element={<Protected role="Student"><StudentPage /></Protected>} />
            <Route path="/creator" element={<Protected role="Creator"><CreatorPage /></Protected>} />
            <Route path="/creator/create-group" element={<Protected role="Creator"><CreatorCreateGroupPage /></Protected>} />
            <Route path="/creator/edit/:groupId" element={<Protected role="Creator"><CreatorEditGroupPage /></Protected>} />
            <Route path="/admin" element={<Protected role="Admin"><AdminPage /></Protected>} />

            <Route
              path="/materials/:groupId"
              element={
                <Protected>
                  <GroupMemberGuard>
                    <MaterialsPage />
                  </GroupMemberGuard>
                </Protected>
              }
            />
            <Route
              path="/materials/:groupId/add"
              element={
                <Protected>
                  <GroupMemberGuard>
                    <MaterialsAddPage />
                  </GroupMemberGuard>
                </Protected>
              }
            />
            
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </GroupAccessProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

