# FinvoraAI: Mobile Developer Onboarding & Master API Guide

This is the **complete and definitive guide** for the FinvoraAI Android (Jetpack Compose) application. It contains all the logic, payloads, and specifications from the shipped web version to ensure 100% feature parity.

---

## 🚀 1. Developer Roadmap (Phase-by-Phase)

### Phase 1: Authentication (Clerk)
**Goal**: Get a valid User ID and Session Token.
1.  **Library**: Add the [Clerk Android SDK](https://clerk.com/docs/quickstarts/android) to `build.gradle`.
2.  **Initialization**: Use your `CLERK_PUBLISHABLE_KEY` in the `Application` class.
3.  **Login Flow**: Use `Clerk.signIn()` or the pre-built UI components.
4.  **Token Retrieval**: After login, call `clerk.session.getToken()` to get the JWT. 
    - *Crucial*: This token is short-lived. Always get a fresh token before making an API call or use an Interceptor.
5.  **Timezone Sync**: On every app launch, sync the user's timezone to Clerk metadata:
    `user.update(unsafeMetadata = mapOf("lastSeenTimezone" to "Asia/Kolkata"))`.

### Phase 2: Core Data (Income & Expenses)
**Goal**: Basic CRUD (Create, Read, Update, Delete) and Dashboard.
1.  **Architecture**: Use **Retrofit** or **Ktor** with a `Bearer Token Interceptor`.
2.  **Endpoints**: Implement REST calls for `/api/get-income`, `/api/add-income`, etc.
3.  **Local State**: Use `StateFlow` to update the UI immediately after a successful `POST`.

### Phase 3: AI Financial Intelligence (Complex Flow)
**Goal**: Handle the "Draft -> Confirm" interaction.
1.  **Chat Interface**: Create a simple chat UI with a `LazyColumn`.
2.  **The "Draft" Card**: If an AI response contains `pendingActions`, render a special "Review Card" with "Confirm", "Edit", and "Cancel" buttons.
3.  **Confirmation Logic**:
    - User clicks **Confirm** -> Send `POST /api/ai-chat` with the `pendingId` and `data` payload.
    - User clicks **Cancel** -> Send `POST /api/ai-chat` with the `message: "cancel"` and `pendingId`.

---

## 🔑 2. Authentication & Headers (Clerk)

**Header Format**:
Every request to the backend must include:
```http
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

---

## 📡 3. API Reference & Payload Structures

### A. Income Endpoints
-   **Get All**: `GET /api/get-income`
-   **Add**: `POST /api/add-income`
    ```json
    {
      "transactionType": "Income",
      "title": "Freelance Pay",
      "emoji": "💰",
      "category": "freelance",
      "amount": "1500.00", // STRING in REST
      "date": "2024-05-12T00:00:00Z",
      "resolveId": "pa_abc123" // Optional: linked to AI draft
    }
    ```
-   **Update**: `PUT /api/update-income/:id`
-   **Delete**: `DELETE /api/delete-income/:id`

### B. Expense Endpoints
-   **Get All**: `GET /api/get-expense`
-   **Add**: `POST /api/add-expenses`
    ```json
    {
      "transactionType": "Expense",
      "title": "Grocery Shopping",
      "emoji": "🛒",
      "category": "food",
      "amount": "85.50", // STRING in REST
      "date": "2024-05-12T00:00:00Z",
      "resolveId": "pa_xyz789" // Optional
    }
    ```
-   **Update**: `PUT /api/update-expenses/:id`
-   **Delete**: `DELETE /api/delete-expenses/:id`

### C. Unified Transactions
-   **Get All**: `GET /api/get-alltransaction`
    - Returns a combined array of Income and Expense objects.

---

## 🤖 4. AI Chat Intelligence (Deep Dive)

### Endpoint: `POST /api/ai-chat`

#### Scenario 1: Sending a Message
```json
{ "message": "I spent $50 on pizza today" }
```

#### Scenario 2: Confirming a Draft
```json
{
  "message": "Confirmed",
  "payload": {
    "pendingId": "pa_xyz123",
    "data": {
      "title": "Pizza Hut",
      "amount": 50.00, // NUMBER in AI Payload
      "category": "food",
      "date": "2024-05-12T00:00:00Z",
      "emoji": "🍕"
    }
  }
}
```

#### Scenario 3: Cancelling a Draft
```json
{
  "message": "cancel",
  "payload": { "pendingId": "pa_xyz123" }
}
```

### AI Response Structure (`ChatResult`)
```json
{
  "reply": "I've drafted that expense for you...",
  "pendingActions": [
    {
      "type": "expense",
      "title": "Pizza Hut",
      "pendingId": "pa_xyz123",
      "amount": 50,
      "emoji": "🍕",
      "category": "food",
      "date": "2024-05-12T00:00:00Z",
      "isAutoLearned": true
    }
  ],
  "metadata": {
    "resolvedStatus": {
      "pa_old_id": "superseded"
    }
  }
}
```

---

## 📊 5. Analytics & Parity Logic

### Dashboard Summary
- **Total Balance**: `Sum(Income.amount) - Sum(Expense.amount)`
- **Total Income**: `Sum(Income.amount)`
- **Total Expense**: `Sum(Expense.amount)`
- **Total Transactions**: `Sum(Income.amount) + Sum(Expense.amount)`

### Visual Specs
- **Income Color**: `#22c55e` (Green)
- **Expense Color**: `#EF5350` (Red)
- **Amount Formatting**: Use the following suffixes for large numbers:
  - `1e3` -> `k` (e.g., $1.5k)
  - `1e6` -> `M` (e.g., $2M)
  - `1e9` -> `B` (e.g., $1B)

---

## 📋 6. Category & Interface Reference

### Income Categories
| Value | Title |
| :--- | :--- |
| `business` | Business |
| `freelance` | Freelance |
| `salary` | Salary |
| `investment` | Investment |
| `rentalIncome` | Rental Income |
| `otherIncome` | Other Income |

### Expense Categories
| Value | Title |
| :--- | :--- |
| `food` | Food |
| `rent` | Rent |
| `utilities` | Utilities |
| `entertainment` | Entertainment |
| `transportation` | Transportation |
| `shopping` | Shopping |
| `healthcare` | Healthcare |
| `otherExpense` | Other Expense |

---

## 🛠️ 7. Common "Gotchas" for Freshers

1.  **Amount Data Type (CRITICAL)**: 
    - **REST Endpoints** (`/api/add-income`): Amount must be a **String** (e.g., `"50.00"`).
    - **AI Payload** (`payload.data.amount`): Amount is a **Number** (e.g., `50.00`).
2.  **Date Format**: Always use **ISO 8601** strings (e.g., `2024-05-12T12:00:00Z`).
3.  **History Pagination**: `GET /api/ai-chat/history?skip=0&limit=10`. 
    - The `history` array returned uses `createdAt` as the timestamp.
4.  **Typing Effect**: Implement a simple delay when displaying AI messages to simulate typing (3 chars every 15ms).
5.  **Superseded Drafts**: If the AI response contains `metadata.resolvedStatus`, check if any old draft IDs in your chat history are marked as `superseded`. If so, gray them out in the UI.

---
**Prepared for**: FinvoraAI Android Development
**Verification**: Verified against Next.js frontend and NestJS backend (May 2026).
