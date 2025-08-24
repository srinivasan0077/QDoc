# InboxIQ - AI-Powered Email Assistant SaaS

**InboxIQ** is an AI-powered email productivity SaaS designed to help teams and enterprises manage, categorize, summarize, and respond to emails intelligently. It aggregates multiple mailboxes, applies AI-driven categorization, and provides optional AI-generated summaries and draft replies.

---

## **Key Features (MVP)**

1. **Multi-Mailbox Aggregation**  
   - Support multiple mailboxes per user or organization.  
   - Fetch emails via **IMAP** using **OAuth2** for Gmail and Outlook.  

2. **Email Categorization**  
   - Automatic categorization of emails based on user-defined or pre-defined categories.  
   - AI-powered categorization using **GPT-3.5-turbo** or other lightweight models.  

3. **Email Storage**  
   - Store full emails and attachments in secure object storage (S3/MinIO).  
   - Maintain metadata (sender, recipient, subject, date, category).  

4. **Recurring Email Fetching**  
   - Configurable jobs for fetching emails every X minutes using **Quartz Scheduler**.  
   - Thread pool per customer to handle multiple mailboxes efficiently.  

5. **Admin & User Dashboard**  
   - View aggregated inbox for all mailboxes.  
   - Monitor categories, summaries, and AI usage.  
   - User & subscription management.  

---

## **Premium Features (Phase 1+)**

1. **AI Summarization**  
   - Generate short summaries of emails for quick reading.  
   - Track token usage to manage AI costs.  

2. **AI Draft Replies**  
   - Suggest draft replies for emails using high-end AI APIs (GPT-4 / Claude).  
   - Optional per-request processing to control costs.  

3. **Attachment Analysis**  
   - Extract text from PDF/DOCX/Images and categorize / summarize.  
   - Optional per-attachment processing for premium users.  

---

## **Technical Stack**

- **Backend**: Spring Boot, Spring Data JPA, Quartz Scheduler  
- **Frontend**: React.js with TailwindCSS / Material UI  
- **Database**: MySQL / PostgreSQL for metadata  
- **Object Storage**: S3 / MinIO for attachments  
- **AI Integration**: OpenAI GPT-3.5/GPT-4 or Claude, optional local embeddings  
- **Email Protocols**: IMAP via Jakarta Mail, OAuth2 authentication  

---

## **Planned Features / Roadmap**

- Analytics & reports for team performance and response times  
- Auto-assignment of emails to teams or departments  
- Hybrid AI model for low-cost categorization + premium AI processing  
- Multi-language support for email categorization & summaries  
- Enterprise SLA, security compliance (GDPR, HIPAA)  

---
