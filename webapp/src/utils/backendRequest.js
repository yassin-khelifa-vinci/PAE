const { getToken } = require('./auths');

async function login(email, password) {
  const response = await fetch(`${process.env.API_BASE_URL}/auths/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      email,
      password,
    }),
  });

  if (!response.ok) {
    throw new Error(`Error while logging in. Response status: ${response.status}`);
  }

  return response.json();
}

async function register(email, password, firstName, lastName, phoneNumber, role) {
  const response = await fetch(`${process.env.API_BASE_URL}/auths/register`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      email,
      password,
      firstName,
      lastName,
      phoneNumber,
      role,
    }),
  });

  if (!response.ok) {
    throw new Error(`Error while registering. Response status: ${response.status}`);
  }

  return response.json();
}

async function getAllContacts() {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/contact/all`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Error fetching contact data. Response status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while fetching contact data. Details: ${error.message}`);
  }
}

async function changeInternshipProject(internshipProject, numVersion) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/stages`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        numVersion,
        internshipProject,
      }),
    });
    if (!response.ok) {
      throw new Error(`Error changing internship project. Response status: ${response.status}`);
    }
  } catch (error) {
    throw new Error(`Error while changing internship project. Details: ${error.message}`);
  }
}

async function getEnterpriseInfo(id) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/enterprise/${id}/info`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Error fetching enterprise data. Response status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while fetching enterprise data. Details: ${error.message}`);
  }
}

async function addContact(idEnterprise) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/contact`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        idEnterprise,
      }),
    });

    if (!response.ok) {
      throw new Error(`Error adding contact status. Response status: ${response.status}`);
    }
  } catch (error) {
    throw new Error(`Error while adding contact. Details: ${error.message}`);
  }
}

async function getLastStage() {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/stages`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });

    if (!response.ok)
      throw new Error(`Error fetching stage data. Response status: ${response.status}`);

    // If no content(204) is returned, return undefined
    if (response.status === 204) {
      return undefined;
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while changing contact status. Details: ${error.message}`);
  }
}

async function getAllEnterprise() {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/enterprise`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });

    if (!response.ok)
      throw new Error(`Error fetching all enterprise. Response status: ${response.status}`);

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while fetching all enterprise. Details: ${error.message}`);
  }
}

async function getAllUsers() {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/auths/all`, {
      method: 'GET',
      headers: {
        Authorization: `${token}`,
      },
    });

    if (!response.ok)
      throw new Error(`Error fetching users data. Response status: ${response.status}`);

    // If no content(204) is returned, return undefined
    if (response.status === 204) {
      return undefined;
    }

    const data = await response.json();

    return data;
  } catch (error) {
    throw new Error(`Error while getting all users data. Details: ${error.message}`);
  }
}

async function blacklistEnterprise(enterpriseId, blacklistReason, versionNumber) {
  try {
    const token = getToken();

    const response = await fetch(
      `${process.env.API_BASE_URL}/enterprise/${enterpriseId}/blacklist`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${token}`,
        },
        body: JSON.stringify({
          blacklistReason,
          versionNumber,
        }),
      },
    );

    if (!response.ok)
      throw new Error(`Error blacklisting enterprise. Response status: ${response.status}`);
  } catch (error) {
    throw new Error(`Error while blacklisting enterprise. Details: ${error.message}`);
  }
}

async function getAllReponsableFromEnterprise() {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/responsableStage/all`, {
      method: 'GET',
      headers: {
        Authorization: `${token}`,
      },
    });

    if (!response.ok)
      throw new Error(`Error fetching users data. Response status: ${response.status}`);

    // If no content(204) is returned, return undefined
    if (response.status === 204) {
      return undefined;
    }

    const data = await response.json();

    return data;
  } catch (error) {
    throw new Error(`Error while getting all users data. Details: ${error.message}`);
  }
}

async function createStage(internshipProject, internshipSupervisorId, contact, signatureDate) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/stages/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        internshipProject,
        internshipSupervisorId,
        contact,
        signatureDate,
      }),
    });

    if (!response.ok) throw new Error(`Error creating stage. Response status: ${response.status}`);
  } catch (error) {
    throw new Error(`Error while creating stage. Details: ${error.message}`);
  }
}

async function createResponsableStage(lastName, firstName, phoneNumber, email, enterprise) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/responsableStage/create`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        lastName,
        firstName,
        phoneNumber,
        email,
        enterprise,
      }),
    });

    if (!response.ok) throw new Error(`Error creating stage. Response status: ${response.status}`);
  } catch (error) {
    throw new Error(`Error while creating stage. Details: ${error.message}`);
  }
}

async function createEnterprise(data) {
  const token = getToken();

  const response = await fetch(`${process.env.API_BASE_URL}/enterprise`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `${token}`,
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) throw new Error(`Error creating enterprise. Response status: ${response.status}`);

  return response.json();
}

async function getStats() {
  const token = getToken();

  const response = await fetch(`${process.env.API_BASE_URL}/stages/stats`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `${token}`,
    },
  });

  if (!response.ok) throw new Error(`${response.status}, ${await response.text()}`);

  return response.json();
}

async function getEnterpriseContactsList(id) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/contact/enterprise/${id}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });
    if (!response.ok)
      throw new Error(`Error fetching enterprise contacts. Response status: ${response.status}`);

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while fetching enterprise contacts. Details: ${error.message}`);
  }
}

async function editData(body) {
    const token = getToken();
    const response = await fetch(`${process.env.API_BASE_URL}/auths`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify(body),
    });

    if (!response.ok)
      throw new Error(`Error fetching users data. Response status: ${response.status}`);

    // If no content(204) is returned, return undefined
    if (response.status === 204) {
      return undefined;
    }

    const data = await response.json();

    return data;
}

async function getUserContact(id) {
  try {
    const token = getToken();
    const response = await fetch(`${process.env.API_BASE_URL}/contact/${id}/contact`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });

    if (!response.ok)
      throw new Error(`Error fetching users contacts. Response status: ${response.status}`);

    // If no content(204) is returned, return undefined
    if (response.status === 204) {
      return undefined;
    }
    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while fetching enterprise contacts. Details: ${error.message}`);
  }
}

async function getEnterprisesStats() {
  try {
    const token = getToken();
    const response = await fetch(`${process.env.API_BASE_URL}/enterprise/stats`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });

    if (!response.ok)
      throw new Error(`Error fetching users data. Response status: ${response.status}`);

    const data = await response.json();

    return data;
  } catch (error) {
    throw new Error(`Error while editing user contacts. Details: ${error.message}`);
  }
}

async function getStage(id) {
  try {
    const token = getToken();
    const response = await fetch(`${process.env.API_BASE_URL}/stages/${id}/getstage`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
    });

    if (!response.ok)
      throw new Error(`Error fetching user s stage. Response status: ${response.status}`);

    // If no content(204) is returned, return undefined
    if (response.status === 204) {
      return undefined;
    }

    const data = await response.json();

    return data;
  } catch (error) {
    throw new Error(`Error while editing user s stage. Details: ${error.message}`);
  }
}

module.exports = {
  login,
  register,
  getAllContacts,
  getEnterpriseInfo,
  getLastStage,
  getAllEnterprise,
  addContact,
  getAllUsers,
  editData,
  changeInternshipProject,
  createEnterprise,
  getStats,
  blacklistEnterprise,
  getEnterpriseContactsList,
  getAllReponsableFromEnterprise,
  createStage,
  createResponsableStage,
  getUserContact,
  getStage,
  getEnterprisesStats,
};
