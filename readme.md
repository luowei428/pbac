# Policy Based Access Control

This is Java based implementation based on [Ladon](https://github.com/ory/ladon)

Policy Based Access Control (PBAC) is similar to [Role Based Access Control](https://en.wikipedia.org/wiki/Role-based_access_control)
or [Access Control Lists](https://en.wikipedia.org/wiki/Access_control_list).
In contrast to [ACL](https://en.wikipedia.org/wiki/Access_control_list) and [RBAC](https://en.wikipedia.org/wiki/Role-based_access_control)
you get fine-grained access control with the ability to answer questions in complex environments such as multi-tenant or distributed applications
and large organizations. PBAC and Ladon are inspired by [AWS IAM Policies](http://docs.aws.amazon.com/IAM/latest/UserGuide/access_policies.html).

# Concepts

PBAC is an access control library that answers the question:

> **Who** is **able** to do **what** on **something** given some **context**

* **Who**: An arbitrary unique subject name, for example "ken" or "printer-service.mydomain.com".
* **Able**: The effect which can be either "allow" or "deny".
* **What**: An arbitrary action name, for example "delete", "create" or "scoped:action:something".
* **Something**: An arbitrary unique resource name, for example "something", "resources.articles.1234" or some uniform
    resource name like "urn:isbn:3827370191".
* **Context**: The current context containing information about the environment such as the IP Address,
    request date, the resource owner name, the department ken is working in or any other information you want to pass along.
    (optional)

To decide what the answer is, PBAC uses policy documents which can be represented as JSON



```json
POST /policies HTTP/1.1
Host: localhost:5000
Content-Type: application/json

{
    "id": "1",
    "description": "One policy to rule them all.",
    "subjects": [
        "users:<peter|ken>",
        "users:maria",
        "groups:admins"
    ],
    "effect": "allow",
    "resources": [
        "resources:articles:<.*>",
        "resources:printer"
    ],
    "actions": [
        "delete",
        "<create|update>"
    ],
    "conditions": {
        "resourceOwner": {
            "type": "EqualsSubjectCondition"
        },
        "isBankUser": {
            "type": "StringEqualCondition",
            "options": {
                "equals": "true"
            }
        }
    }
}
```

and can answer access requests that look like:

```json
 POST /policies/isAllowed HTTP/1.1
 Host: localhost:5000
 Content-Type: application/json
 
 
 {
 	"subject":"users:ken",
 	"resource":"resources:articles:1234",
 	"action":"create",
 	"context":{
 		"isBankUser":"true",
 		"resourceOwner":"users:ken"
 	}
 }
```

Default implementation is using H2 in-memory database (http://localhost:5000/h2-console/) and exposes REST endpoint on http://localhost:5000/policies

