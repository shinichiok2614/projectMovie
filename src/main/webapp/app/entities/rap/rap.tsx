import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './rap.reducer';

export const Rap = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const rapList = useAppSelector(state => state.rap.entities);
  const loading = useAppSelector(state => state.rap.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="rap-heading" data-cy="RapHeading">
        <Translate contentKey="projectMovieApp.rap.home.title">Raps</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.rap.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/rap/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.rap.home.createLabel">Create new Rap</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {rapList && rapList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.rap.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('tenRap')}>
                  <Translate contentKey="projectMovieApp.rap.tenRap">Ten Rap</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenRap')} />
                </th>
                <th className="hand" onClick={sort('diaChi')}>
                  <Translate contentKey="projectMovieApp.rap.diaChi">Dia Chi</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('diaChi')} />
                </th>
                <th className="hand" onClick={sort('thanhPho')}>
                  <Translate contentKey="projectMovieApp.rap.thanhPho">Thanh Pho</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('thanhPho')} />
                </th>
                <th>
                  <Translate contentKey="projectMovieApp.rap.cumRap">Cum Rap</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {rapList.map((rap, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/rap/${rap.id}`} color="link" size="sm">
                      {rap.id}
                    </Button>
                  </td>
                  <td>{rap.tenRap}</td>
                  <td>{rap.diaChi}</td>
                  <td>{rap.thanhPho}</td>
                  <td>{rap.cumRap ? <Link to={`/cum-rap/${rap.cumRap.id}`}>{rap.cumRap.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/rap/${rap.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/rap/${rap.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/rap/${rap.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="projectMovieApp.rap.home.notFound">No Raps found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Rap;
